package com.bwongo.core.student_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.*;
import static com.bwongo.core.student_mgt.utils.StudentMsgConstant.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
public record StudentRequestDto(
          String firstName,
          String secondName,
          String schoolIdNumber,
          String nationalIdNumber,
          String email,
          String studentClass,
          Long schoolId,
          boolean canBeNotified
) {
    public void validate(){
        Validate.notEmpty(firstName, NULL_FIRST_NAME);
        Validate.notEmpty(secondName, NULL_SECOND_NAME);
        Validate.notEmpty(schoolIdNumber, NULL_SCHOOL_CARD_ID);
        //Validate.notEmpty(email, NULL_EMAIL);
        Validate.notEmpty(studentClass, NULL_STUDENT_CLASS);
        Validate.notNull(schoolId, ExceptionType.BAD_REQUEST, NULL_SCHOOL_ID);
        Validate.notNull(canBeNotified, ExceptionType.BAD_REQUEST, NULL_CAN_BE_NOTIFIED);

        if(!email.isEmpty())
            StringRegExUtil.stringOfEmail(email, INVALID_EMAIL, email);
    }
}
