package com.nc.sms_notification.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 1/8/24
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenResponseDto {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    private String scope;

    private String generatedInZone;

    private Object user;
}
