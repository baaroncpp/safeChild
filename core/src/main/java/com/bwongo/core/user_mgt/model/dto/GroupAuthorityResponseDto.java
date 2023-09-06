package com.bwongo.core.user_mgt.model.dto;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public record GroupAuthorityResponseDto(
         Long id,
         Date createdOn,
         Date modifiedOn,
         UserGroupResponseDto userGroup,
         PermissionResponseDto permission) {
}
