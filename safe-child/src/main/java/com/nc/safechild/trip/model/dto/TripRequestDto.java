package com.nc.safechild.trip.model.dto;

import com.nc.safechild.exceptions.model.ExceptionType;
import com.nc.safechild.base.utils.Validate;

import static com.nc.safechild.base.utils.MessageConstants.*;
import static com.nc.safechild.base.utils.WebServiceUtil.isTripType;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/18/23
 **/
public record TripRequestDto(
        String username,
        Long schoolId,
        String tripType,
        String note
) {
    public void validate(){
        Validate.notEmpty(username, NULL_USERNAME);
        Validate.notNull(schoolId, ExceptionType.BAD_REQUEST, NULL_SCHOOL_ID);
        Validate.notNull(tripType, ExceptionType.BAD_REQUEST, NULL_TRIP_TYPE);
        Validate.isTrue(isTripType(tripType), ExceptionType.BAD_REQUEST, INVALID_TRIP_TYPE);
    }
}
