package com.bwongo.core.user_mgt.model.dto;

import com.bwongo.core.base.model.enums.UserTypeEnum;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/15/23
 **/
public record UserResponseDto(
         Long id,
         Date createdOn,
         Date modifiedOn,
         String username,
         boolean accountLocked,
         boolean accountExpired,
         boolean credentialExpired,
         boolean approved,
         UserGroupResponseDto userGroup,
         Boolean isDeleted,
         Long approvedBy,
         UserTypeEnum userType,
         Long userMetaId
) {
}
