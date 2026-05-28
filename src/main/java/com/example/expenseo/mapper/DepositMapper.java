package com.example.expenseo.mapper;

import com.example.expenseo.dto.DepositRequest;
import com.example.expenseo.dto.DepositResponse;
import com.example.expenseo.models.DepositModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepositMapper {

    DepositModel toEntity(DepositRequest request);

    DepositResponse toResponse(DepositModel deposit);
}
