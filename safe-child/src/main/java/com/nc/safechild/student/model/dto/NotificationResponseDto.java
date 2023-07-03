package com.nc.safechild.student.model.dto;


import com.nc.safechild.student.model.enums.StudentStatus;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/29/23
 **/
public record NotificationResponseDto(
        String transactionStatus,
        StudentStatus studentStatus,
        String appRef
) {
}
