package com.bwongo.core.security.models;

import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/6/23
 **/
@Setter
public class AppClient implements ClientDetails {
    private String name;
    private Boolean isSecretRequired;
    private String secret;
    private Boolean isScoped;
    private Set<String> scopes;
    private Set<String> authorizedGrantTypes;
    private Set<String> registeredRedirectUri;
    private Map<String,Object> additionalInformation;
    private Boolean isAutoApprove;
    private Integer refreshTokenValiditySeconds;
    private List<GrantedAuthority> authorities;

    @Override
    public String getClientId() {
        return name;
    }

    @Override
    public Set<String> getResourceIds() {
        return null;
    }

    @Override
    public boolean isSecretRequired() {
        return isSecretRequired;
    }

    @Override
    public String getClientSecret() {
        return secret;
    }

    @Override
    public boolean isScoped() {
        return isScoped;
    }

    @Override
    public Set<String> getScope() {
        return scopes;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        return isAutoApprove;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }
}

