package com.bwongo.core.security.utils;

import com.bwongo.commons.models.utils.MapUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import com.bwongo.core.security.models.LoginUser;

import java.time.ZoneId;
import java.util.Map;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/6/23
 **/
public class UserDetailsTokenEnhancer  implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map info = MapUtils.create("generatedInZone", ZoneId.systemDefault().toString()).build();

        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
        LoginUser principal = (LoginUser)authentication.getPrincipal();
        principal.setPassword(null);
        info.put("user",principal);
        token.setAdditionalInformation(info);
        return token;
    }
}

