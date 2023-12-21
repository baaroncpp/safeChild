package com.nc.sms_notification.network;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 6/30/23
 **/
@Service
@Slf4j
public class WebClientService {

    @Value("${pandora-sms.url}")
    private String pandoraSmsUrl;

    @Value("${pandora-sms.username}")
    private String username;

    @Value("${pandora-sms.password}")
    private String password;

    @Value("${pandora-sms.sender}")
    private String sender;

    @Value("${pandora-sms.message-type}")
    private String messageType;

    @Value("${pandora-sms.message-category}")
    private String messageCategory;

    public String makeSmsCall(String number, String message){

        System.out.println(sender);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("username", username);
        formData.add("password", password);
        formData.add("sender", sender);
        formData.add("message_type", messageType);
        formData.add("message_category", messageCategory);
        formData.add("number", number);
        formData.add("message", message);

        return WebClient.create()
                .post()
                .uri(pandoraSmsUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();
    }

    public Object makeSmsPayment(){
        return null;
    }
}
