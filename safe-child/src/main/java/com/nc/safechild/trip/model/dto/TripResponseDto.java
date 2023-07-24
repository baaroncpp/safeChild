package com.nc.safechild.trip.model.dto;

import com.nc.safechild.student.models.enums.NotificationRoleEnum;
import com.nc.safechild.trip.model.enums.TripStatus;
import com.nc.safechild.trip.model.enums.TripType;

import java.util.Date;
import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/17/23
 **/
public record TripResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        TripType tripType,
        TripStatus tripStatus,
        String diverUsername,
        List<NotificationRoleEnum> roles

) {
}
