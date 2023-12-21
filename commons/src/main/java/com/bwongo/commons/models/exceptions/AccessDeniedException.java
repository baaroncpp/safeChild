package com.bwongo.commons.models.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/29/23
 **/
@Slf4j
public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message, Object ... messageConstants){
        super(String.format(message, messageConstants));
        log.error(String.format(message, messageConstants));
    }

    public AccessDeniedException(String message) {
        super(message);
        log.error(message);
    }
}
