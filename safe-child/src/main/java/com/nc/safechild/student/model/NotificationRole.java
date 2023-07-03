package com.nc.safechild.student.model;

import com.nc.safechild.student.model.enums.NotificationRoleEnum;
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
public class NotificationRole {
    private NotificationRoleEnum role;
    private String note;
}
