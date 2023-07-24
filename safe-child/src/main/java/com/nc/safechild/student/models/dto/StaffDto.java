package com.nc.safechild.student.models.dto;


import com.nc.safechild.student.models.NotificationRole;
import com.nc.safechild.student.models.enums.UserType;

import java.util.List;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/28/23
 **/
public record StaffDto(
        Long id,
        String username,
        String name,
        String email,
        String phoneNumber,
        String schoolName,
        UserType userType,
        List<NotificationRole> roles
) {
}
