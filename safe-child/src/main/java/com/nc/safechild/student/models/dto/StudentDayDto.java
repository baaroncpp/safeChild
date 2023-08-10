package com.nc.safechild.student.models.dto;


import com.nc.safechild.base.utils.Validate;
import com.nc.safechild.exceptions.model.ExceptionType;

import static com.nc.safechild.base.utils.MessageConstants.*;
import static com.nc.safechild.base.utils.WebServiceUtil.isStudentStatus;
import static com.nc.safechild.base.utils.WebServiceUtil.isValidDate;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/10/23
 **/
public record StudentDayDto(
        String date,
        String schoolId,
        String studentStatus
) {
    public void validate(){
        Validate.isTrue(isValidDate(date), ExceptionType.BAD_REQUEST, INVALID_DATE);
        Validate.notNull(schoolId, ExceptionType.BAD_REQUEST, NULL_SCHOOL_ID);
        Validate.notEmpty(studentStatus, NULL_STUDENT_STATUS);
        Validate.isTrue(isStudentStatus(studentStatus), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS, studentStatus);
    }
}
