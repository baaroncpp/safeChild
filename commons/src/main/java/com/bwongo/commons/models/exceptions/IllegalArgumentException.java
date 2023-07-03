package com.bwongo.commons.models.exceptions;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 4/27/23
 **/
public class IllegalArgumentException extends RuntimeException {
    public IllegalArgumentException(String message, Object... messageConstants) {
        super(String.format(message, messageConstants));
    }

    public IllegalArgumentException(String message) {
        super(message);
    }
}
