package com.bwongo.core.student_mgt.model.dto;

import com.bwongo.core.school_mgt.model.dto.SchoolResponseDto;
import com.bwongo.core.user_mgt.model.dto.UserResponseDto;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
public record StudentResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        UserResponseDto createdBy,
        UserResponseDto modifiedBy,
        String firstName,
        String secondName,
        String schoolIdNumber,
        String nationalIdNumber,
        String email,
        String studentClass,
        SchoolResponseDto school,
        String profileImagePathUrl,
        String idImagePathUrl,
        boolean canBeNotified,
        String physicalAddress,
        String studentUsername
) {
}
