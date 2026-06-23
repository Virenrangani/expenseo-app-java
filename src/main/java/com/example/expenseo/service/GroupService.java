package com.example.expenseo.service;

import com.example.expenseo.dto.GroupRequest;
import com.example.expenseo.dto.GroupResponse;
import com.example.expenseo.mapper.GroupMapper;
import com.example.expenseo.models.GroupModel;
import com.example.expenseo.models.UserModel;
import com.example.expenseo.repository.GroupRepository;
import com.example.expenseo.repository.UserRepository;
import com.example.expenseo.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMapper groupMapper;

    @Transactional
    public GroupResponse createGroup(GroupRequest request){

        CustomUserDetails userDetails =
                (CustomUserDetails) SecurityContextHolder.getContext()
                                .getAuthentication().getPrincipal();
        UserModel currentUser = userDetails.getUser();

        Set<UserModel> members = new HashSet<>();
        members.add(currentUser);

        if(request.getMemberEmails() != null){
            List<UserModel> users = userRepository.findByEmailIn(request.getMemberEmails());
            members.addAll(users);
        }

        GroupModel group = GroupModel.builder()
                        .name(request.getName())
                        .members(new HashSet<>(members))
                        .build();

        GroupModel saved = groupRepository.save(group);
        return groupMapper.toResponse(saved);
    }

    public List<GroupResponse> getAllGroups(){
        CustomUserDetails userDetails = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String currentUserId = userDetails.getUser().getId();

        List<GroupModel> groups = groupRepository.findByMembersIdOrderByCreatedAtDesc(currentUserId);

        return  groupMapper.toResponseList(groups);
    }

    public void deleteGroup(String id) {
        if (!groupRepository.existsById(id)){
            throw new RuntimeException("Group is not exist by this id"+id);
        }
        groupRepository.deleteById(id);
    }
}
