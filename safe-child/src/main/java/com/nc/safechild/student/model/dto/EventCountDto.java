package com.nc.safechild.student.model.dto;

import com.nc.safechild.student.model.enums.NotificationRoleEnum;

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
