package com.bwongo.core.trip_mgt.model.dto;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 2/21/24
 **/
public record StudentOnTripDto(
        TripResponseDto trip,
        List<StudentEventLocationDto> studentEvents
) {
}
