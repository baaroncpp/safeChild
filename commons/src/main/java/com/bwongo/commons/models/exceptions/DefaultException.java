package com.bwongo.commons.models.exceptions;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 4/11/23
 **/
public class DefaultException  extends RuntimeException{
    public DefaultException(String message, Object ... messageConstants){
        super(String.format(message, messageConstants));
    }

    public DefaultException(String message) {
        super(message);
    }
}

