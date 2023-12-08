package com.bwongo.commons.models.utils;

import com.bwongo.commons.models.exceptions.BadCredentialsException;
import com.bwongo.commons.models.exceptions.BadRequestException;
import com.bwongo.commons.models.exceptions.InsufficientAuthenticationException;
import com.bwongo.commons.models.exceptions.ResourceNotFoundException;
import com.bwongo.commons.models.exceptions.model.AccessDeniedException;
import com.bwongo.commons.models.exceptions.model.ExceptionType;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static com.bwongo.commons.models.utils.ConstantMessages.*;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 3/22/23
 **/
public class Validate {
    private Validate() {}

    public static void isTrue(Object errorClass, boolean value, ExceptionType exceptionType, String message, Object ... params){
        if (!value && exceptionType.equals(ExceptionType.BAD_REQUEST)) {
            throw new BadRequestException(errorClass, message, params);
        }

        if (!value && exceptionType.equals(ExceptionType.RESOURCE_NOT_FOUND)) {
            throw new ResourceNotFoundException(errorClass, message, params);
        }

        if (!value && exceptionType.equals(ExceptionType.BAD_CREDENTIALS)) {
            throw new BadCredentialsException(errorClass, message, params);
        }

        if (!value && exceptionType.equals(ExceptionType.INSUFFICIENT_AUTH)) {
            throw new InsufficientAuthenticationException(errorClass, message, params);
        }

        if (!value && exceptionType.equals(ExceptionType.ACCESS_DENIED)) {
            throw new AccessDeniedException(message, params);
        }

    }

    public static void notNull(Object errorClass, Object value, ExceptionType exceptionType, String message, Object ... params){

        if (value == null && exceptionType.equals(ExceptionType.BAD_REQUEST)) {
            throw new BadRequestException(errorClass, message, params);
        }

        if (value == null && exceptionType.equals(ExceptionType.RESOURCE_NOT_FOUND)) {
            throw new ResourceNotFoundException(errorClass, message, params);
        }

        if (value == null && exceptionType.equals(ExceptionType.BAD_CREDENTIALS)) {
            throw new BadCredentialsException(errorClass, message, params);
        }

        if (value == null && exceptionType.equals(ExceptionType.INSUFFICIENT_AUTH)) {
            throw new InsufficientAuthenticationException(errorClass, message, params);
        }

        if (value == null && exceptionType.equals(ExceptionType.ACCESS_DENIED)) {
            throw new AccessDeniedException(message, params);
        }

    }

    public static void notEmpty(Object errorClass, String value, String message, Object ... params){
        if (!StringUtils.hasLength(value)) {
            throw new BadRequestException(errorClass, message, params);
        }
    }

    public static void isPresent(Object errorClass, Optional<?> value, String message, Object ... params){
        if(value.isEmpty()){
            throw new ResourceNotFoundException(errorClass, String.format(message,params));
        }
    }

    private static void checkForbiddenWord(Object object, String word){
        boolean anyMatch = FORBIDDEN_WORDS.stream()
                .anyMatch(fw -> fw.contains(word));

        if(anyMatch){
            throw new BadRequestException(object, String.format(IS_FORBIDDEN_WORD, word));
        }
    }

    public static void doesObjectContainFields(Object errorClass, Object object, List<String> fields){
        for(String value : fields){
            Validate.isTrue(errorClass, doesObjectContainField(object, value), ExceptionType.BAD_REQUEST , String.format(IS_INVALID_FIELD, value));
        }
    }

    public static boolean doesObjectContainField(Object object, String fieldName) {
        Class<?> objectClass = object.getClass();
        for (Field field : objectClass.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    public static void filterException(Object errorClass, String message){
        if(org.apache.commons.lang3.StringUtils.containsIgnoreCase(message, "found")){
            throw new ResourceNotFoundException(errorClass, message);
        }else if(org.apache.commons.lang3.StringUtils.containsIgnoreCase(message, "Could not send Message")){
            throw new BadRequestException(errorClass, message + " Core banking service cannot be accessed");
        }else {
            throw new BadRequestException(errorClass, message);
        }
    }

    public static void isAcceptableDateFormat(Object errorClass, String stringDate){
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        try {
            sdf.parse(stringDate);
        } catch (ParseException e) {
            throw new BadRequestException(errorClass, "invalid date format, use: yyyy-MM-dd HH:mm:ss");
        }
    }
}
