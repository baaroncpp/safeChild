package com.nc.safechild.utils;

import com.nc.safechild.exceptions.BadRequestException;
import com.nc.safechild.exceptions.InsufficientAuthenticationException;
import com.nc.safechild.exceptions.ResourceNotFoundException;
import com.nc.safechild.exceptions.model.ExceptionType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 3/22/23
 **/
public class Validate {
    private Validate() {}

    public static void isTrue(boolean value, ExceptionType exceptionType, String message, Object ... params){
        if (!value && exceptionType.equals(ExceptionType.BAD_REQUEST)) {
            throw new BadRequestException(message, params);
        }

        if (!value && exceptionType.equals(ExceptionType.RESOURCE_NOT_FOUND)) {
            throw new ResourceNotFoundException(message, params);
        }

        if (!value && exceptionType.equals(ExceptionType.BAD_CREDENTIALS)) {
            throw new BadCredentialsException(message);
        }

        if (!value && exceptionType.equals(ExceptionType.INSUFFICIENT_AUTH)) {
            throw new InsufficientAuthenticationException(message, params);
        }

    }

    public static void notNull(Object value, ExceptionType exceptionType, String message, Object ... params){

        if (value == null && exceptionType.equals(ExceptionType.BAD_REQUEST)) {
            throw new BadRequestException(message, params);
        }

        if (value == null && exceptionType.equals(ExceptionType.RESOURCE_NOT_FOUND)) {
            throw new ResourceNotFoundException(message, params);
        }

        if (value == null && exceptionType.equals(ExceptionType.BAD_CREDENTIALS)) {
            throw new BadCredentialsException(message);
        }

        if (value == null && exceptionType.equals(ExceptionType.INSUFFICIENT_AUTH)) {
            throw new InsufficientAuthenticationException(message, params);
        }

    }

    public static void notEmpty(String value, String message, Object ... params){
        if (!StringUtils.hasLength(value)) {
            throw new BadRequestException(message, params);
        }
    }

    public static void isPresent(Optional<?> value, String message, Object ... params){
        if(value.isEmpty()){
            throw new ResourceNotFoundException(String.format(message,params));
        }
    }
}
