package com.nc.sms_notification.network;

import com.nc.sms_notification.exceptions.BadRequestException;
import com.nc.sms_notification.model.dto.AccessTokenResponseDto;
import com.nc.sms_notification.model.dto.SmsPaymentRequestDto;
import com.nc.sms_notification.model.dto.SmsPaymentResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 1/8/24
 **/
@Service
@Slf4j
public class WebClientNCCoreService {

    @Value("${url.access.token}")
    private String accessTokenUrl;

    @Value("${url.sms.payment}")
    private String smsPaymentUrl;

    @Value("${core.username}")
    private String username;

    @Value("${core.password}")
    private String password;

    @Value("${core.client-id}")
    private String clientId;

    @Value("${core.secret}")
    private String clientSecret;

    private static final String PASSWORD_STRING = "password";
    private static final String USERNAME_STRING = "username";
    private static final String GRANT_TYPE_STRING = "grant_type";

    public String getAccessToken(){

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(USERNAME_STRING, username);
        formData.add(PASSWORD_STRING, password);
        formData.add(GRANT_TYPE_STRING, PASSWORD_STRING);

        return WebClient.create()
                .post()
                .uri(accessTokenUrl)
                .headers(headers -> headers.setBasicAuth(clientId, clientSecret))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();
    }

    public SmsPaymentResponseDto makePayment(SmsPaymentRequestDto smsPaymentRequestDto){

        var stringResponse = getAccessToken();
        var jsonObj = new JSONObject(stringResponse);

        if (!jsonObj.has("access_token")) {
            throw new BadRequestException("FAILED ");
        }

        String accessToken = jsonObj.getString("access_token");

        return WebClient.create()
                .post()
                .uri(smsPaymentUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(smsPaymentRequestDto)
                .retrieve()
                .bodyToMono(SmsPaymentResponseDto.class)
                .block();
    }
}
