package com.example.expenseo.mapper;

import com.example.expenseo.dto.GroupRequest;
import com.example.expenseo.dto.GroupResponse;
import com.example.expenseo.models.GroupModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING , uses = UserMapStructMapper.class)
public interface GroupMapper {

     GroupResponse toEntity(GroupRequest request);

     List<GroupModel> toResponse(GroupResponse response);
}
