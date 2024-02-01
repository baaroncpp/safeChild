package com.bwongo.core.notify_mgt.model.dto;

import com.bwongo.core.base.model.enums.NotificationRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/28/23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRoleDto {
    private NotificationRoleEnum role;
    private String note;
}
