package com.bwongo.core.security.utils;

import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/6/23
 **/
public class CustomOauth2RequestFactory  extends DefaultOAuth2RequestFactory {
    public CustomOauth2RequestFactory(ClientDetailsService clientDetailsService) {
        super(clientDetailsService);
    }
}

