package com.nc.safechild.student.models.dto;


import com.nc.safechild.student.models.enums.StudentStatus;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/29/23
 **/
public record NotificationResponseDto(
        String transactionStatus,
        StudentStatus studentStatus,
        String appRef,
        String schoolId
) {
}
