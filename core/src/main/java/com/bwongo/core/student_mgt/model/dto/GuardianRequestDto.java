package com.bwongo.core.student_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.student_mgt.utils.StudentMsgConstant;

import static com.bwongo.core.base.utils.EnumValidations.isIdentificationType;
import static com.bwongo.core.base.utils.EnumValidations.isRelation;
import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.NULL_PHONE_NUMBER;
import static com.bwongo.core.student_mgt.utils.StudentMsgConstant.NULL_IS_NOTIFIED;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.INVALID_PHONE_NUMBER;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
public record GuardianRequestDto(
        String fullName,
        String phoneNumber,
        String address,
        String relation,
        String identificationType,
        String idNumber,
        Long studentId,
        boolean isNotified
) {
    public void validate(){
        Validate.notEmpty(this, fullName, StudentMsgConstant.NULL_FULL_NAME);
        Validate.notEmpty(this, phoneNumber, NULL_PHONE_NUMBER);
        StringRegExUtil.stringOfInternationalPhoneNumber(this, phoneNumber, INVALID_PHONE_NUMBER);
        Validate.notEmpty(this, address, StudentMsgConstant.NULL_ADDRESS);
        Validate.notEmpty(this, relation, StudentMsgConstant.NULL_RELATION);
        Validate.isTrue(this, isRelation(relation), ExceptionType.BAD_REQUEST, StudentMsgConstant.INVALID_RELATION);
        Validate.notNull(this, studentId, ExceptionType.BAD_REQUEST, StudentMsgConstant.NULL_STUDENT_ID);
        Validate.notNull(this, isNotified, ExceptionType.BAD_REQUEST, NULL_IS_NOTIFIED);

        if(!identificationType.isEmpty())
            Validate.isTrue(this, isIdentificationType(identificationType), ExceptionType.BAD_REQUEST, StudentMsgConstant.INVALID_IDENTIFICATION_TYPE);
    }
}
