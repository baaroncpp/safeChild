package com.nc.safechild.student.models.dto;

import com.nc.safechild.student.models.enums.NotificationRoleEnum;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/6/23
 **/
public record EventCountDto(
        Date date,
        NotificationRoleEnum role,
        Long dateCount
) {
}
