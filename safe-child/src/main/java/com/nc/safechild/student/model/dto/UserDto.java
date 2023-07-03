package com.nc.safechild.student.model.dto;


import com.nc.safechild.student.model.NotificationRole;
import com.nc.safechild.student.model.enums.UserType;

import java.util.List;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/28/23
 **/
public record UserDto(
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
