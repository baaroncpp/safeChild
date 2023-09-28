package com.nc.safechild.student.models.dto;

import com.nc.safechild.exceptions.model.ExceptionType;
import com.nc.safechild.base.utils.Validate;
import com.nc.safechild.base.utils.WebServiceUtil;

import static com.nc.safechild.base.utils.MessageConstants.*;
import static com.nc.safechild.base.utils.MessageConstants.NULL_APP_REF;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/18/23
 **/
public record NotificationDto(
        String studentUsername,
        String studentStatus,
        String performedByUsername,
        String appRef,
        double latitudeCoordinate,
        double longitudeCoordinate
) {
    public void validate(){
        Validate.notEmpty(studentUsername, NULL_USERNAME);
        Validate.notEmpty(studentStatus, NULL_STUDENT_STATUS);
        Validate.isTrue(WebServiceUtil.isStudentStatus(studentStatus), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS, studentStatus);
        Validate.notEmpty(performedByUsername, NULL_PERFORMED_BY);
        Validate.notEmpty(appRef, NULL_APP_REF);
        Validate.notNull(latitudeCoordinate, ExceptionType.BAD_REQUEST, NULL_COORDINATE);
        Validate.notNull(longitudeCoordinate, ExceptionType.BAD_REQUEST, NULL_COORDINATE);
    }
}
