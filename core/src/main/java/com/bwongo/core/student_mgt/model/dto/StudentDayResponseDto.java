package com.bwongo.core.student_mgt.model.dto;

import com.bwongo.core.base.model.dto.LocationResponseDto;
import com.bwongo.core.base.model.enums.StudentStatus;
import com.bwongo.core.base.model.jpa.TLocation;
import com.bwongo.core.school_mgt.model.dto.SchoolResponseDto;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import com.bwongo.core.user_mgt.model.dto.UserResponseDto;
import com.bwongo.core.user_mgt.model.jpa.TUser;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/10/23
 **/
public record StudentDayResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        UserResponseDto createdBy,
        UserResponseDto modifiedBy,
        StudentStatus studentStatus,
        UserResponseDto staff,
        StudentResponseDto student,
        SchoolResponseDto school,
        Date schoolDate,
        boolean onTrip,
        LocationResponseDto location
) {
}
