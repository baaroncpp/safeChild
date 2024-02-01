package com.bwongo.commons.models.exceptions;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 4/27/23
 **/
public class IllegalArgumentException extends RuntimeException {
    private Object errorClass;
    public IllegalArgumentException(Object errorClass, String message, Object... messageConstants) {
        super(String.format(message, messageConstants));
        this.errorClass = errorClass;
    }

    public IllegalArgumentException(Object errorClass, String message) {
        super(message);
        this.errorClass = errorClass;
    }

    public Object getErrorClass() {
        return errorClass;
    }
}
