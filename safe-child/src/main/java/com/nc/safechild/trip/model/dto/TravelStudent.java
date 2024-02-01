package com.nc.safechild.trip.model.dto;

import com.nc.safechild.base.utils.Validate;
import com.nc.safechild.exceptions.model.ExceptionType;

import static com.nc.safechild.base.utils.MessageConstants.*;
import static com.nc.safechild.base.utils.WebServiceUtil.isStudentStatus;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/10/23
 **/
public record TravelStudent(
        Long tripId,
        String studentStatus
) {
    public void validate(){
        Validate.notNull(tripId, ExceptionType.BAD_REQUEST, NULL_TRIP_ID);
        Validate.notEmpty(studentStatus, NULL_STUDENT_STATUS);
        Validate.isTrue(isStudentStatus(studentStatus), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS, studentStatus);
    }
}
