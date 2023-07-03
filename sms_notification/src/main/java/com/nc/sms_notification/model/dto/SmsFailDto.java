package com.nc.sms_notification.model.dto;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/2/23
 **/
public record SmsFailDto(
    boolean success,
    String error_message
) {
}
