package com.bwongo.core.user_mgt.model.dto;

import com.bwongo.core.base.model.enums.ApprovalEnum;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/15/23
 **/
public record UserApprovalResponseDto(
         Long id,
         Date createdOn,
         Date modifiedOn,
         UserResponseDto createdBy,
         UserResponseDto modifiedBy,
         UserResponseDto user,
         ApprovalEnum status
) {
}
