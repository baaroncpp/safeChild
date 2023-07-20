package com.nc.safechild.utils;

import com.nc.safechild.exceptions.model.ExceptionType;
import com.nc.safechild.student.model.enums.StudentStatus;
import com.nc.safechild.student.model.enums.TripType;
import lombok.extern.slf4j.Slf4j;
import nl.strohalm.cyclos.webservices.CyclosWebServicesClientFactory;
import nl.strohalm.cyclos.webservices.model.MemberVO;
import org.springframework.beans.factory.annotation.Value;

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

    public static CyclosWebServicesClientFactory getWebServiceFactory(){
        var factory = new CyclosWebServicesClientFactory();
        factory.setServerRootUrl("http://127.0.0.1:8080/platform");
        factory.setUsername("web");
        factory.setPassword("web@1234");
        return factory;
    }

    public static MemberVO getUserByUsername(String username){
        var memberWebService = WebServiceUtil.getWebServiceFactory().getMemberWebService();

        MemberVO result = null;
        try {
            result = memberWebService.loadByUsername(username);
        }catch(Exception e){
            var errorMsg = e.getMessage();
            Validate.filterException(errorMsg.substring(errorMsg.lastIndexOf(":") + 1));
        }
        Validate.notNull(result, ExceptionType.RESOURCE_NOT_FOUND, MessageConstants.STUDENT_NOT_FOUND, username);

        return result;
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

    public static boolean isTripType(String value){
        List<String> tripTypeList = Arrays.asList(
                TripType.PICKUP.name(),
                TripType.DROP_OFF.name()
        );

        return tripTypeList.contains(value);
    }


}
