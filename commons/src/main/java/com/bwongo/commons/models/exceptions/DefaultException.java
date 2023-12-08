package com.bwongo.commons.models.exceptions;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 4/11/23
 **/
public class DefaultException  extends RuntimeException{
    private Object errorClass;
    public DefaultException(Object errorClass, String message, Object ... messageConstants){
        super(String.format(message, messageConstants));
        this.errorClass = errorClass;
    }

    public DefaultException(Object errorClass, String message) {
        super(message);
        this.errorClass = errorClass;
    }

    public Object getErrorClass() {
        return errorClass;
    }
}

