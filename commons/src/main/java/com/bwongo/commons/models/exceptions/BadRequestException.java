package com.bwongo.commons.models.exceptions;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 3/22/23
 **/
@Slf4j
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message, Object ... messageConstants){
        super(String.format(message, messageConstants));
        log.error(String.format(message, messageConstants));
    }

    public BadRequestException(String message) {
        super(message);
        log.error(message);
    }
}
