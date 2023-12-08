package com.bwongo.commons.models.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 4/11/23
 **/
@Slf4j
public class BadCredentialsException  extends RuntimeException{

    private Object errorClass;
    public BadCredentialsException(Object errorClass, String message, Object ... messageConstants){
        super(String.format(message, messageConstants));
        log.error(String.format(message, messageConstants));
        this.errorClass = errorClass;
    }

    public BadCredentialsException(Object errorClass, String message) {
        super(message);
        log.error(message);
        this.errorClass = errorClass;
    }

    public Object getErrorClass() {
        return errorClass;
    }
}
