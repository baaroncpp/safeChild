package com.bwongo.core.student_mgt.model.dto;

import com.bwongo.core.user_mgt.model.dto.UserResponseDto;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
public record StudentGuardianDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        UserResponseDto createdBy,
        UserResponseDto modifiedBy,
        StudentResponseDto student,
        GuardianResponseDto guardian
) {
}
