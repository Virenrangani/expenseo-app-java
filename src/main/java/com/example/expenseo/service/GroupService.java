package com.example.expenseo.service;

import com.example.expenseo.dto.AuthResponse;
import com.example.expenseo.dto.GroupRequest;
import com.example.expenseo.dto.GroupResponse;
import com.example.expenseo.models.GroupModel;
import com.example.expenseo.models.UserModel;
import com.example.expenseo.repository.GroupRepository;
import com.example.expenseo.repository.UserRepository;
import com.example.expenseo.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    @Transactional
    public GroupResponse createGroup(GroupRequest request){
        /// 1. Get the current logged-in user (The Creator)
        CustomUserDetails userDetails = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserModel currentUser = userDetails.getUser();

        /// Initialize the Group
        GroupModel group = GroupModel.builder()
                .name(request.getName())
                .build();

        /// 3. Add the creator as the first default member!
        group.getMembers().add(currentUser);

        /// 4. Search for additional members by email and add them
        if (request.getMemberEmails() != null && !request.getMemberEmails().isEmpty()){
            for (String email : request.getMemberEmails()){
                UserModel member = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Cannot create group: No user found with email " + email));

                group.getMembers().add(member);
            }
        }
        GroupModel savedGroup = groupRepository.save(group);

        return GroupResponse.builder()
                .id(savedGroup.getId())
                .name(savedGroup.getName())
                .createdAt(savedGroup.getCreatedAt())
                .members(savedGroup.getMembers().stream()
                        .map(member -> AuthResponse.builder()
                                .id(member.getId())
                                .name(member.getName())
                                .email(member.getEmail())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
