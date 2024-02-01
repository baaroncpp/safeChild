package com.bwongo.core.student_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.*;
import static com.bwongo.core.student_mgt.utils.StudentMsgConstant.*;
import static com.bwongo.core.base.utils.BasicMsgConstants.*;

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
          boolean canBeNotified,
          String physicalAddress,
          String qrCodeString
) {
    public void validate(){
        Validate.notEmpty(this, firstName, NULL_FIRST_NAME);
        Validate.notEmpty(this, secondName, NULL_SECOND_NAME);
        Validate.notEmpty(this, schoolIdNumber, NULL_SCHOOL_CARD_ID);
        Validate.notEmpty(this, studentClass, NULL_STUDENT_CLASS);
        Validate.notNull(this, schoolId, ExceptionType.BAD_REQUEST, NULL_SCHOOL_ID);
        Validate.notNull(this, canBeNotified, ExceptionType.BAD_REQUEST, NULL_CAN_BE_NOTIFIED);
        Validate.notEmpty(this, physicalAddress, NULL_PHYSICAL_ADDRESS);

        if(!email.isEmpty())
            StringRegExUtil.stringOfEmail(this, email, INVALID_EMAIL, email);

        if(!nationalIdNumber.isEmpty())
            Validate.isTrue(this,nationalIdNumber.length() < 20, ExceptionType.BAD_REQUEST, VALUE_TOO_LONG, "nationalIdNumber", nationalIdNumber.length());
    }
}
