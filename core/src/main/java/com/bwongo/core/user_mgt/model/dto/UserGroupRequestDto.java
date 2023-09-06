package com.bwongo.core.user_mgt.model.dto;

import com.bwongo.commons.models.text.StringRegExUtil;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public record UserGroupRequestDto(
        String name,
        String note
) {
    public void validate(){
        Validate.notEmpty(name, USER_GROUP_NAME_IS_NULL);
        Validate.notEmpty(note, USER_GROUP_NOTE_IS_NULL);
        StringRegExUtil.stringOfOnlyUpperCase(name, USER_GROUP_NAME_ONLY_UPPER_CASE);
    }
}
