package com.bwongo.core.vehicle_mgt.model.dto;

import com.bwongo.core.school_mgt.model.dto.SchoolResponseDto;
import com.bwongo.core.user_mgt.model.dto.UserResponseDto;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/29/23
 **/
public record VehicleResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        UserResponseDto createdBy,
        UserResponseDto modifiedBy,
        UserResponseDto currentDriver,
        String plateNumber,
        String vehicleModel,
        boolean onRoute,
        SchoolResponseDto school,
        int maximumCapacity
) {
}
