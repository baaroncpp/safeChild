package com.bwongo.core.trip_mgt.model.dto;

import com.bwongo.core.base.model.dto.LocationResponseDto;
import com.bwongo.core.base.model.enums.StudentStatus;
import com.bwongo.core.student_mgt.model.dto.StudentResponseDto;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/11/23
 **/
public record StudentEventLocationDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        StudentStatus studentStatus,
        StudentResponseDto studentResponseDto,
        LocationResponseDto location
) {
}
