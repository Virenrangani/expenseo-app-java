package com.example.expenseo.mapper;

import com.example.expenseo.dto.SavingRequest;
import com.example.expenseo.dto.SavingResponse;
import com.example.expenseo.models.SavingModel;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" ,builder = @Builder(disableBuilder = true))
public interface SavingMapper {

    SavingModel toRequest(SavingRequest request);

    @Mapping(source = "user.id" , target = "userId")
    SavingResponse toResponse(SavingModel saving);

}
