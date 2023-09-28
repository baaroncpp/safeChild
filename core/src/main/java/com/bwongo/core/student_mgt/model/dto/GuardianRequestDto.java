package com.bwongo.core.student_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.student_mgt.utils.StudentMsgConstant;

import static com.bwongo.core.base.utils.EnumValidations.isIdentificationType;
import static com.bwongo.core.base.utils.EnumValidations.isRelation;
import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.NULL_PHONE_NUMBER;

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
        Long studentId
) {
    public void validate(){
        Validate.notEmpty(fullName, StudentMsgConstant.NULL_FULL_NAME);
        Validate.notEmpty(phoneNumber, NULL_PHONE_NUMBER);
        Validate.notEmpty(address, StudentMsgConstant.NULL_ADDRESS);
        Validate.notEmpty(relation, StudentMsgConstant.NULL_RELATION);
        Validate.isTrue(isRelation(relation), ExceptionType.BAD_REQUEST, StudentMsgConstant.INVALID_RELATION);
        Validate.notNull(studentId, ExceptionType.BAD_REQUEST, StudentMsgConstant.NULL_STUDENT_ID);

        if(!identificationType.isEmpty())
            Validate.isTrue(isIdentificationType(identificationType), ExceptionType.BAD_REQUEST, StudentMsgConstant.INVALID_IDENTIFICATION_TYPE);
    }
}
