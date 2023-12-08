package com.bwongo.core.base.exceptions;

import com.bwongo.commons.models.exceptions.*;
import com.bwongo.commons.models.exceptions.model.AccessDeniedException;
import com.bwongo.commons.models.exceptions.model.ExceptionPayLoad;
import com.bwongo.core.base.model.enums.LogLevelEnum;
import com.bwongo.core.base.model.jpa.TLog;
import com.bwongo.core.base.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 4/27/23
 **/
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionsHandler {

    private final LogService logService;

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException badRequestException, HttpServletRequest request){

        var httpStatus = HttpStatus.BAD_REQUEST;

        var exceptionPayLoad = new ExceptionPayLoad(
                request.getRequestURI(),
                badRequestException.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        createLog(badRequestException.getMessage(), badRequestException.getErrorClass().getClass().getName(), httpStatus.toString(), request.getRequestURI());

        return new ResponseEntity<>(exceptionPayLoad, httpStatus);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException, HttpServletRequest request){

        var httpStatus = HttpStatus.NOT_FOUND;

        var exceptionPayLoad = new ExceptionPayLoad(
                request.getRequestURI(),
                resourceNotFoundException.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        createLog(resourceNotFoundException.getMessage(), resourceNotFoundException.getErrorClass().getClass().getName(), httpStatus.toString(), request.getRequestURI());


        return new ResponseEntity<>(exceptionPayLoad, httpStatus);
    }

    @ExceptionHandler(value = {InsufficientAuthenticationException.class})
    public ResponseEntity<Object> handleInsufficientAuthenticationException(InsufficientAuthenticationException insufficientAuthenticationException, HttpServletRequest request){

        var httpStatus = HttpStatus.FORBIDDEN;

        var exceptionPayLoad = new ExceptionPayLoad(
                request.getRequestURI(),
                insufficientAuthenticationException.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        createLog(insufficientAuthenticationException.getMessage(), insufficientAuthenticationException.getErrorClass().getClass().getName(), httpStatus.toString(), request.getRequestURI());

        return new ResponseEntity<>(exceptionPayLoad, httpStatus);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException badCredentialsException, HttpServletRequest request){

        var httpStatus = HttpStatus.UNAUTHORIZED;

        var exceptionPayLoad = new ExceptionPayLoad(
                request.getRequestURI(),
                badCredentialsException.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        createLog(badCredentialsException.getMessage(), badCredentialsException.getErrorClass().getClass().getName(), httpStatus.toString(), request.getRequestURI());

        return new ResponseEntity<>(exceptionPayLoad, httpStatus);
    }

    @ExceptionHandler(value = {DefaultException.class})
    public ResponseEntity<Object> handleDefaultException(DefaultException defaultException, HttpServletRequest request){

        var httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        var exceptionPayLoad = new ExceptionPayLoad(
                request.getRequestURI(),
                defaultException.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        createLog(defaultException.getMessage(), defaultException.getErrorClass().getClass().getName(), httpStatus.toString(), request.getRequestURI());

        return new ResponseEntity<>(exceptionPayLoad, httpStatus);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException accessDeniedException, HttpServletRequest request){

        var httpStatus = HttpStatus.valueOf(403);

        var exceptionPayLoad = new ExceptionPayLoad(
                request.getRequestURI(),
                accessDeniedException.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        createLog(accessDeniedException.getMessage(), "", httpStatus.toString(), request.getRequestURI());

        return new ResponseEntity<>(exceptionPayLoad, httpStatus);
    }

    private void createLog(String note, String errorClass, String httpStatus, String url){
        var log = new TLog();
        log.setLogLevel(LogLevelEnum.ERROR);
        log.setNote(note);
        log.setEntityName(errorClass);
        log.setHttpStatus(httpStatus);
        log.setResourceUrl(url);

        logService.recordLog(log);
    }
}
