package com.example.expenseo.mapper;

import com.example.expenseo.dto.ExpenseRequest;
import com.example.expenseo.dto.ExpenseResponse;
import com.example.expenseo.models.ExpenseModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExpenseMapper {

    ExpenseModel toEntity (ExpenseRequest request);

    ExpenseResponse toResponse (ExpenseModel expense);
}
