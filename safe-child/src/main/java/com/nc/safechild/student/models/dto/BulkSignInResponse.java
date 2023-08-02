package com.nc.safechild.student.models.dto;

import com.nc.safechild.trip.model.jpa.Trip;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/2/23
 **/
public record BulkSignInResponse(
        Trip trip,
        String status,
        String note
) {
}
