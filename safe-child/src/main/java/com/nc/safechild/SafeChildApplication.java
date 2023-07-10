package com.nc.safechild;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.TimeZone;

@SpringBootApplication
public class SafeChildApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafeChildApplication.class, args);
	}
}
