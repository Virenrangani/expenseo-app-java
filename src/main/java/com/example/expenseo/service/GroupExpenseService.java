package com.example.expenseo.service;

import com.example.expenseo.dto.GroupExpenseRequest;
import com.example.expenseo.dto.GroupExpenseResponse;
import com.example.expenseo.dto.SettlementRequest;
import com.example.expenseo.enums.SplitExpenseType;
import com.example.expenseo.mapper.GroupExpenseMapper;
import com.example.expenseo.models.ExpenseSplitModel;
import com.example.expenseo.models.GroupExpenseModel;
import com.example.expenseo.models.GroupModel;
import com.example.expenseo.models.UserModel;
import com.example.expenseo.repository.GroupExpenseRepository;
import com.example.expenseo.repository.GroupRepository;
import com.example.expenseo.repository.UserRepository;
import com.example.expenseo.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupExpenseService {

    private final GroupExpenseRepository groupExpenseRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupExpenseMapper groupExpenseMapper;

    @Transactional // CRITICAL: Ensures all DB operations succeed, or everything rolls back
    public GroupExpenseResponse createGroupExpense(GroupExpenseRequest request) {

        // 1. Extract Current User (The person logging the expense)
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel currentUser = userDetails.getUser();

        // 2. Fetch the Group and validate the current user is actually in it! (Security Check)
        GroupModel group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        boolean isCurrentUserMember = group.getMembers().stream()
                .anyMatch(member -> member.getId().equals(currentUser.getId()));

        if (!isCurrentUserMember) {
            throw new RuntimeException("Unauthorized: You are not a member of this group.");
        }

        // 3. Fetch the user who paid
        UserModel paidByUser = userRepository.findById(request.getPaidByUserId())
                .orElseThrow(() -> new RuntimeException("PaidBy user not found"));

        if (!group.getMembers().contains(paidByUser)) {
            throw new RuntimeException("Invalid Request: The person who paid must be a member of the group.");
        }

        // 4. MATHEMATICAL VALIDATION: Do the splits add up exactly to the total?
        BigDecimal totalSplitAmount = request.getSplits().stream()
                .map(GroupExpenseRequest.SplitDetailRequest::getAmountOwed)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // compareTo() returns 0 if they are exactly equal.
        if (totalSplitAmount.compareTo(request.getAmount()) != 0) {
            throw new RuntimeException("Mathematical Error: The sum of the split amounts ("
                    + totalSplitAmount + ") does not equal the total expense amount (" + request.getAmount() + ").");
        }

        // 5. Initialize the Parent Expense (The Receipt)
        GroupExpenseModel groupExpense = GroupExpenseModel.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .splitExpenseType(request.getSplitExpenseType())
                .group(group)
                .paidBy(paidByUser)
                .splitModels(new ArrayList<>()) // Initialize empty list
                .build();

        // 6. Process the Splits (The Ledger)
        for (GroupExpenseRequest.SplitDetailRequest splitRequest : request.getSplits()) {

            UserModel splitUser = userRepository.findById(splitRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("Split user not found: " + splitRequest.getUserId()));

            if (!group.getMembers().contains(splitUser)) {
                throw new RuntimeException("Invalid Request: User " + splitUser.getName() + " is not in the group.");
            }

            // Create the child split record
            ExpenseSplitModel splitModel = ExpenseSplitModel.builder()
                    .user(splitUser)
                    .amountOwed(splitRequest.getAmountOwed())
                    .expense(groupExpense) // Link back to parent!
                    .build();

            groupExpense.getSplitModels().add(splitModel);
        }

        // 7. Save to Database (Because we used CascadeType.ALL on the Parent, this saves the children automatically!)
        GroupExpenseModel savedExpense = groupExpenseRepository.saveAndFlush(groupExpense);

        // 8. Map to Response DTO
        GroupExpenseResponse response = groupExpenseMapper.toResponse(savedExpense);

        // 9. THE BULLETPROOF FIX: Map the nested splits manually
        List<GroupExpenseResponse.SplitDetailResponse> splitResponses = savedExpense.getSplitModels().stream()
                .map(split -> GroupExpenseResponse.SplitDetailResponse.builder()
                        .id(split.getId())
                        .userId(split.getUser().getId())
                        .amountOwed(split.getAmountOwed())
                        .build())
                .collect(Collectors.toList());

        // 10. Attach the splits to the response and return!
        response.setSplits(splitResponses);

        return response;
    }

    public List<GroupExpenseResponse> getGroupExpense(String groupId){
        /// 1. Extract Current User
        CustomUserDetails userDetails = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel currentUser = userDetails.getUser();

        /// 2. Fetch the Group and perform the Security Check
        GroupModel group = groupRepository.findById(groupId)
                .orElseThrow(()->new RuntimeException("Group not found"));

        boolean isMember = group.getMembers().stream()
                .anyMatch(member->member.getId().equals(currentUser.getId()));

        if (!isMember){
            throw new RuntimeException("Unauthorized: You cannot view expenses for a group you are not in.");
        }

        List<GroupExpenseModel> expenses = groupExpenseRepository.findByGroupIdOrderByCreatedAtDesc(groupId);

        return expenses.stream().map(expense-> {

            GroupExpenseResponse response = groupExpenseMapper.toResponse(expense);

            List<GroupExpenseResponse.SplitDetailResponse> splitExpense = expense.getSplitModels().stream().map(
                    split-> GroupExpenseResponse.SplitDetailResponse.builder()
                            .id(split.getId())
                            .amountOwed(split.getAmountOwed())
                            .userId(split.getUser().getId())
                            .build()
            ).toList();
            response.setSplits(splitExpense);
            return response;
                }).collect(Collectors.toList());
    }

    public GroupExpenseResponse settleUp(SettlementRequest request){
        // 1. Fetch the Group
        GroupModel group = groupRepository.findById(request.getGroupId())
                .orElseThrow(()-> new RuntimeException("Group is not found"));

        // 2. Fetch Both Users
        UserModel payer = userRepository.findById(request.getPayerId())
                .orElseThrow(()->new RuntimeException("Payer is not found"));

        UserModel receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(()->new RuntimeException("Receiver is not found"));

        // 3. Security: Ensure both users are actually in this group!
        if (!group.getMembers().contains(payer) || !group.getMembers().contains(receiver)){
            throw  new RuntimeException("Both users must be members of the group to settle up.");
        }

        // 4. THE MAGIC MATH: Create the Settlement Transaction
        GroupExpenseModel settlementExpense = GroupExpenseModel.builder()
                .title(payer.getId() + " Paid " + receiver.getName())
                .splitExpenseType(SplitExpenseType.SETTLEMENT)
                .amount(request.getAmount())
                .group(group)
                .paidBy(payer)
                .splitModels(new ArrayList<>())
                .build();

        // 5. Create the exact ledger split for Viren

        ExpenseSplitModel settlementSplit = ExpenseSplitModel.builder()
                .user(receiver)
                .amountOwed(request.getAmount())
                .expense(settlementExpense)
                .build();
        settlementExpense.getSplitModels().add(settlementSplit);

        // 6. Save and Flush to DB
        GroupExpenseModel savedSettlementExpense = groupExpenseRepository.saveAndFlush(settlementExpense);

        // 7. Use the same Bulletproof mapping we used before to return the response
        GroupExpenseResponse response = groupExpenseMapper.toResponse(savedSettlementExpense);

        List<GroupExpenseResponse.SplitDetailResponse> splitExpense = savedSettlementExpense.getSplitModels()
                .stream().map(
                        expense -> GroupExpenseResponse.SplitDetailResponse.builder()
                                .id(expense.getId())
                                .userId(expense.getUser().getId())
                                .amountOwed(expense.getAmountOwed())
                                .build()
                ).toList();
        response.setSplits(splitExpense);

        return response;
    }
}