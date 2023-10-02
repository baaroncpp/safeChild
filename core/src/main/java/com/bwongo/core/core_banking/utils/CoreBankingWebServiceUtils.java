package com.bwongo.core.core_banking.utils;

import nl.strohalm.cyclos.webservices.CyclosWebServicesClientFactory;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/2/23
 **/
public class CoreBankingWebServiceUtils {

    private CoreBankingWebServiceUtils() { }

    private static final String CORE_BANKING_URL = "http://127.0.0.1:8080/platform";

    public static CyclosWebServicesClientFactory getWebServiceFactory(){
        var factory = new CyclosWebServicesClientFactory();
        factory.setServerRootUrl(CORE_BANKING_URL);
        //factory.setUsername("web");
        //factory.setPassword("web@1234");
        return factory;
    }
}
