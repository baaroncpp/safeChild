package com.nc.safechild.exceptions.model;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * @Author bkaaron
 * @Project bega
 * @Date 3/22/23
 **/
public record ExceptionPayLoad(String uri, String message, HttpStatus httpStatus, ZonedDateTime timeStamp) {
}
