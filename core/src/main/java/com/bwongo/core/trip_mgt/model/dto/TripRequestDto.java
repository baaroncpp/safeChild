package com.bwongo.core.trip_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.base.utils.EnumValidations.isTripType;
import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.NULL_USERNAME;
import static com.bwongo.core.trip_mgt.utils.TripMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/18/23
 **/
public record TripRequestDto(
        String tripType,
        String note
) {
    public void validate(){
        Validate.notNull(tripType, ExceptionType.BAD_REQUEST, NULL_TRIP_TYPE);
        Validate.isTrue(isTripType(tripType), ExceptionType.BAD_REQUEST, INVALID_TRIP_TYPE);
    }
}
