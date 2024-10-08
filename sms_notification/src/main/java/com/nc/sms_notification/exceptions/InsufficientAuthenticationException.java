package com.nc.sms_notification.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 4/11/23
 **/
@Slf4j
public class InsufficientAuthenticationException extends RuntimeException{
    public InsufficientAuthenticationException(String message, Object ... messageConstants){
        super(String.format(message, messageConstants));
        log.error(String.format(message, messageConstants));
    }

    public InsufficientAuthenticationException(String message) {
        super(message);
        log.error(message);
    }
}
