package com.example.expenseo.mapper;

import com.example.expenseo.dto.GroupResponse;
import com.example.expenseo.models.GroupModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING , uses = UserMapStructMapper.class)
public interface GroupMapper {

     GroupResponse toResponse(GroupModel group);

     List<GroupResponse> toResponseList(List<GroupModel> groups);
}
