package com.bwongo.core.student_mgt.model.dto;

import com.bwongo.core.base.model.enums.IdentificationType;
import com.bwongo.core.base.model.enums.Relation;
import com.bwongo.core.user_mgt.model.dto.UserResponseDto;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
public record GuardianResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        UserResponseDto createdBy,
        UserResponseDto modifiedBy,
        String fullName,
        String phoneNumber,
        String address,
        Relation relation,
        IdentificationType identificationType,
        String idNumber
) {
}
