package com.example.expenseo.mapper;

import com.example.expenseo.dto.GroupExpenseResponse;
import com.example.expenseo.models.ExpenseSplitModel;
import com.example.expenseo.models.GroupExpenseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

// componentModel = "spring" tells MapStruct to make this a Spring Bean (@Component)
// so we can @Inject it into our services.
@Mapper(componentModel = "spring")
public interface GroupExpenseMapper {

    /**
     * Maps the Parent Expense Entity to the Response DTO.
     * MapStruct automatically knows how to map lists, so it will automatically
     * call 'toSplitResponse' for every item in the splits list!
     */
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "paidBy.id", target = "paidByUserId")
    GroupExpenseResponse toResponse(GroupExpenseModel expenseModel);

    /// Maps the Child Split Entity to the nested SplitDetailResponse DTO.
    @Mapping(source = "user.id", target = "userId")
    GroupExpenseResponse.SplitDetailResponse toSplitResponse(ExpenseSplitModel splitModel);
}