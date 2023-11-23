package com.bwongo.core.notify_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.base.utils.EnumValidations.isStudentStatus;
import static com.bwongo.core.notify_mgt.utils.NotificationMsgConstants.*;
import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.*;
import static com.bwongo.core.trip_mgt.utils.TripMsgConstants.NULL_STUDENT_STATUS;
import static com.bwongo.core.trip_mgt.utils.TripMsgConstants.*;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/27/23
 **/
public record NotificationDriverDto(
        Long tripId,
        String studentUsername,
        String studentStatus,
        String appRef,
        double latitudeCoordinate,
        double longitudeCoordinate
) {
    public void validate(){
        Validate.notNull(tripId, ExceptionType.BAD_REQUEST, NULL_TRIP_ID);
        Validate.notEmpty(studentUsername, NULL_USERNAME);
        Validate.notEmpty(studentStatus, NULL_STUDENT_STATUS);
        Validate.isTrue(isStudentStatus(studentStatus), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS, studentStatus);
        Validate.notEmpty(appRef, NULL_APP_REF);
        Validate.notNull(latitudeCoordinate, ExceptionType.BAD_REQUEST, NULL_COORDINATE);
        Validate.notNull(longitudeCoordinate, ExceptionType.BAD_REQUEST, NULL_COORDINATE);
    }
}
