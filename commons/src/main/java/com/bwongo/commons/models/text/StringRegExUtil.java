package com.bwongo.commons.models.text;

import com.bwongo.commons.models.exceptions.BadRequestException;

import java.util.regex.Pattern;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 12/24/22
 */
public class StringRegExUtil {

    public static void stringOfStandardPassword(String value, String message){
        if(!Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$", value)){
            throw new BadRequestException(message);
        }
    }

    public static void stringOfOnlyNumbers(String value, String message){
        if(!Pattern.matches("^[0-9]+$", value)){
            throw new BadRequestException(message);
        }
    }

    public static void stringOfOnlyNumbersAndChars(String value, String message){
        if(!Pattern.matches("^[a-zA-Z0-9]+$", value)){
            throw new BadRequestException(message);
        }
    }

    public static void stringOfOnlyCharsNoneCaseSensitive(String value, String message){
        if(!Pattern.matches("^[a-zA-Z]+$", value)){
            throw new BadRequestException(message);
        }
    }

    public static void stringOfOnlyCharsNoneCaseSensitiveAndOneSpace(String value, String message){
        if(!Pattern.matches("^[a-zA-Z ]+$", value)){
            throw new BadRequestException(message);
        }
    }

    public static void stringOfOnlyLowerCase(String value, String message){
        if(!Pattern.matches("^[a-z]+$", value)){
            throw new BadRequestException(message);
        }
    }

    public static void stringOfOnlyUpperCase(String value, String message){
        if(!Pattern.matches("^[A-Z]+$", value)){
            throw new BadRequestException(message);
        }
    }

    public static void stringOfInternationalPhoneNumber(String value, String message, Object ... params){
        if(!Pattern.matches("^\\+?[1-9][0-9]{7,14}$", value)){
            throw new BadRequestException(message, params);
        }
    }

    public static void stringOfEmail(String value, String message, Object ... params){
        if(!Pattern.matches("^(.+)@(.+)$", value)){
            throw new BadRequestException(message, params);
        }
    }
}
