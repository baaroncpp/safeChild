package com.bwongo.core.security.utils;

import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/6/23
 **/
@Component
public class CustomAccessTokenConverter  extends JwtAccessTokenConverter {

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication authentication = super.extractAuthentication(map);
        // User is my custom UserDetails class
        final Map<String, Object> userMap = (Map)map.get("user");

        authentication.setDetails(userMap);
        return authentication;
    }

}
