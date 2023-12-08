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
        Validate.notNull(this, tripId, ExceptionType.BAD_REQUEST, NULL_TRIP_ID);
        Validate.notEmpty(this, studentUsername, NULL_USERNAME);
        Validate.notEmpty(this, studentStatus, NULL_STUDENT_STATUS);
        Validate.isTrue(this, isStudentStatus(studentStatus), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS, studentStatus);
        Validate.notEmpty(this, appRef, NULL_APP_REF);
        Validate.notNull(this, latitudeCoordinate, ExceptionType.BAD_REQUEST, NULL_COORDINATE);
        Validate.notNull(this, longitudeCoordinate, ExceptionType.BAD_REQUEST, NULL_COORDINATE);
    }
}
