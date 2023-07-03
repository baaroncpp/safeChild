package com.nc.safechild.student.model.dto;

import com.nc.safechild.exceptions.model.ExceptionType;
import com.nc.safechild.utils.Validate;
import com.nc.safechild.utils.WebServiceUtil;

import static com.nc.safechild.utils.MessageConstants.*;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/27/23
 **/
public record NotificationDto(
        String studentUsername,
        String studentStatus,
        String performedByUsername,
        String appRef
) {
    public void validate(){
        Validate.notEmpty(studentUsername, NULL_USERNAME);
        Validate.notEmpty(studentStatus, NULL_STUDENT_STATUS);
        Validate.isTrue(WebServiceUtil.isStudentStatus(studentStatus), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS, studentStatus);
        Validate.notEmpty(performedByUsername, NULL_PERFORMED_BY);
        Validate.notEmpty(appRef, NULL_APP_REF);
    }
}
