package com.bwongo.commons.models.text;

import com.bwongo.commons.models.exceptions.BadRequestException;

import java.util.regex.Pattern;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 12/24/22
 */
public class StringRegExUtil {

    public static void stringOfStandardPassword(Object errorClass, String value, String message){
        if(!Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$", value)){
            throw new BadRequestException(errorClass, message);
        }
    }

    public static void stringOfOnlyNumbers(Object errorClass,String value, String message){
        if(!Pattern.matches("^[0-9]+$", value)){
            throw new BadRequestException(errorClass, message);
        }
    }

    public static void stringOfOnlyNumbersAndChars(Object errorClass,String value, String message){
        if(!Pattern.matches("^[a-zA-Z0-9]+$", value)){
            throw new BadRequestException(errorClass, message);
        }
    }

    public static void stringOfOnlyCharsNoneCaseSensitive(Object errorClass,String value, String message){
        if(!Pattern.matches("^[a-zA-Z]+$", value)){
            throw new BadRequestException(errorClass, message);
        }
    }

    public static void stringOfOnlyCharsNoneCaseSensitiveAndOneSpace(Object errorClass,String value, String message){
        if(!Pattern.matches("^[a-zA-Z ]+$", value)){
            throw new BadRequestException(errorClass, message);
        }
    }

    public static void stringOfOnlyLowerCase(Object errorClass, String value, String message){
        if(!Pattern.matches("^[a-z]+$", value)){
            throw new BadRequestException(errorClass, message);
        }
    }

    public static void stringOfOnlyUpperCase(Object errorClass, String value, String message){
        if(!Pattern.matches("^[A-Z]+$", value)){
            throw new BadRequestException(errorClass, message);
        }
    }

    public static void stringOfInternationalPhoneNumber(Object errorClass, String value, String message, Object ... params){
        if(!Pattern.matches("^\\+?[1-9][0-9]{7,14}$", value)){
            throw new BadRequestException(errorClass, message, params);
        }
    }

    public static void stringOfEmail(Object errorClass, String value, String message, Object ... params){
        if(!Pattern.matches("^(.+)@(.+)$", value)){
            throw new BadRequestException(errorClass, message, params);
        }
    }
}
