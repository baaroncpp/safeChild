package com.bwongo.commons.models.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 4/11/23
 **/
@Slf4j
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message, Object ... messageConstants){
        super(String.format(message, messageConstants));
        log.error(String.format(message, messageConstants));
    }

    public ResourceNotFoundException(String message) {
        super(message);
        log.error(message);
    }
}
