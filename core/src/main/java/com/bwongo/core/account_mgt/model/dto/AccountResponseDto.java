package com.bwongo.core.account_mgt.model.dto;

import com.bwongo.core.account_mgt.model.enums.AccountStatus;
import com.bwongo.core.school_mgt.model.dto.SchoolResponseDto;
import com.bwongo.core.user_mgt.model.dto.UserResponseDto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/14/23
 **/
public record AccountResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        UserResponseDto createdBy,
        UserResponseDto modifiedBy,
        String accountName,
        String accountNumber,
        AccountStatus status,
        SchoolResponseDto school,
        BigDecimal currentBalance,
        boolean isSchoolAccount
) {
}
