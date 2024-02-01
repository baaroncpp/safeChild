package com.bwongo.core.trip_mgt.model.dto;

import com.bwongo.core.base.model.enums.StudentStatus;

import java.util.Map;

public record TripStatisticsDto(
        TripResponseDto trip,
        Map<StudentStatus, Integer> studentStatuses
) {

}
