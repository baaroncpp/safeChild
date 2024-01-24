package com.bwongo.core.notify_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.NULL_COORDINATE;
import static com.bwongo.core.trip_mgt.utils.TripMsgConstants.NULL_TRIP_ID;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/6/23
 **/
public record BulkSignInRequestDto(
        Long tripId,
        double latitudeCoordinate,
        double longitudeCoordinate
) {
    public void validate(){
        Validate.notNull(this, tripId, ExceptionType.BAD_REQUEST, NULL_TRIP_ID);
        Validate.notNull(this, latitudeCoordinate, ExceptionType.BAD_REQUEST, NULL_COORDINATE);
        Validate.notNull(this, longitudeCoordinate, ExceptionType.BAD_REQUEST, NULL_COORDINATE);
    }
}
