package com.bwongo.core.trip_mgt.model.dto;


import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.base.utils.EnumValidations.isStudentStatus;
import static com.bwongo.core.trip_mgt.utils.TripMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/10/23
 **/
public record TravelStudentDto(
        Long tripId,
        String studentStatus
) {
    public void validate(){
        Validate.notNull(this, tripId, ExceptionType.BAD_REQUEST, NULL_TRIP_ID);
        Validate.notEmpty(this, studentStatus, NULL_STUDENT_STATUS);
        Validate.isTrue(this, isStudentStatus(studentStatus), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS, studentStatus);
    }
}
