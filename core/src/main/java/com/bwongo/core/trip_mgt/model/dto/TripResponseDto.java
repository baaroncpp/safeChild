package com.bwongo.core.trip_mgt.model.dto;

import java.util.Date;
import com.bwongo.core.base.model.enums.*;
import com.bwongo.core.user_mgt.model.dto.SchoolUserResponseDto;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/11/23
 **/
public record TripResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        TripType tripType,
        TripStatus tripStatus,
        SchoolUserResponseDto driver
) {
}
