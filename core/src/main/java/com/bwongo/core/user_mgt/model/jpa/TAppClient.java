package com.bwongo.core.user_mgt.model.jpa;


import javax.persistence.*;
import com.bwongo.core.base.model.jpa.BaseEntity;

import java.util.Date;

/**
 * @author bkaaron
 * @created 16/06/2022
 * @project kabangali
 */
@Entity
@Table(name = "t_app_client", schema = "core")
public class TAppClient extends BaseEntity {
    private String secret;
    private String name;
    private Boolean isSecretRequired;
    private Boolean isScoped;
    private String scope;
    private String grantTypes;
    private String redirectUri;
    private String authorities;
    private Integer tokenValidity;
    private Boolean isEnabled;
    private Date disabledOn;
    private String note;

    @Column(name = "secret")
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Column(name = "is_secret_required")
    public Boolean getSecretRequired() {
        return isSecretRequired;
    }

    public void setSecretRequired(Boolean secretRequired) {
        isSecretRequired = secretRequired;
    }

    @Column(name = "is_scoped")
    public Boolean getScoped() {
        return isScoped;
    }

    public void setScoped(Boolean scoped) {
        isScoped = scoped;
    }

    @Column(name = "grant_types")
    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    @Column(name = "redirect_uri")
    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    @Column(name = "authorities")
    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    @Column(name = "token_validity")
    public Integer getTokenValidity() {
        return tokenValidity;
    }

    public void setTokenValidity(Integer tokenValidity) {
        this.tokenValidity = tokenValidity;
    }

    @Column(name = "is_enabled")
    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getDisabledOn() {
        return disabledOn;
    }

    public void setDisabledOn(Date disabledOn) {
        this.disabledOn = disabledOn;
    }

    @Column(name = "note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "scope")
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}