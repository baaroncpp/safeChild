package com.bwongo.core.user_mgt.model.dto;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public record PermissionResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        RoleResponseDto role,
        String name,
        Boolean isAssignable
) {
}
