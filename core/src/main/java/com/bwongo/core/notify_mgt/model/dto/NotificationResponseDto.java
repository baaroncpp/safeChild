package com.bwongo.core.notify_mgt.model.dto;

import com.bwongo.core.base.model.enums.StudentStatus;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/29/23
 **/
public record NotificationResponseDto(
        Long id,
        StudentStatus studentStatus,
        String schoolName
) {
}
