package com.bwongo.core.notify_mgt.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.notify_mgt.utils.NotificationMsgConstants.NULL_MESSAGE;
import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.NULL_PHONE_NUMBER;
import static com.bwongo.core.school_mgt.utils.SchoolMsgConstants.NULL_SCHOOL_ID;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.INVALID_PHONE_NUMBER;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/2/24
 * @Time 9:11 AM
 **/
public record SmsNotificationRequestDto(
        Long schoolId,
        String phoneNumber,
        String message
) {
    public void validate(){
        Validate.notEmpty(this, phoneNumber, NULL_PHONE_NUMBER);
        StringRegExUtil.stringOfInternationalPhoneNumber(this, phoneNumber, INVALID_PHONE_NUMBER);
        Validate.notEmpty(this, message, NULL_MESSAGE);
        Validate.notNull(this, schoolId, ExceptionType.BAD_REQUEST, NULL_SCHOOL_ID);
    }
}
