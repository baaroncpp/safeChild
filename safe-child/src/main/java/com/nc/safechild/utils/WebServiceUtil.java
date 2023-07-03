package com.nc.safechild.utils;

import com.nc.safechild.student.model.enums.StudentStatus;
import lombok.extern.slf4j.Slf4j;
import nl.strohalm.cyclos.webservices.CyclosWebServicesClientFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/20/23
 **/
@Slf4j
public class WebServiceUtil {

    @Value("${url.core-banking-url}")
    private String coreBankingUrl;

    private static final String SECRET_KEY = "CZKGZ9JO2T4OOPQOWET2";
    private static final String SALT = "ssshhhhhhhhhhh!!!!";

    public static CyclosWebServicesClientFactory getWebServiceFactory(){
        var factory = new CyclosWebServicesClientFactory();
        factory.setServerRootUrl("http://127.0.0.1:8080/platform");
        factory.setUsername("web");
        factory.setPassword("web@1234");
        return factory;
    }

    public static boolean isStudentStatus(String value){
        List<String> studentStatusList = Arrays.asList(
                StudentStatus.OFF_SCHOOL.name(),
                StudentStatus.PICK_UP.name(),
                StudentStatus.ON_SCHOOL.name(),
                StudentStatus.DROP_OFF.name(),
                StudentStatus.IN_CLASS.name()
        );
        return studentStatusList.contains(value);
    }


}
