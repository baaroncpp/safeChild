package com.bwongo.core.notify_mgt.model.dto;

import com.bwongo.core.trip_mgt.model.dto.TripResponseDto;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/2/23
 **/
public record BulkSignInResponse(
        TripResponseDto trip,
        String status,
        String note
) {
}
