package com.nc.sms_notification.api;

import com.nc.sms_notification.model.dto.SmsPaymentRequestDto;
import com.nc.sms_notification.network.WebClientNCCoreService;
import com.nc.sms_notification.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/2/23
 **/
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class TestApi {

    private final SmsService smsService;
    private final WebClientNCCoreService webClientNCCoreService;

    @GetMapping(path = "/test/{number}/message/{message}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object testSms(@PathVariable("number") String number,
                          @PathVariable("message") String message){
        return smsService.testSms(number, message);
    }

    @GetMapping(path = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object testToken(){
        return webClientNCCoreService.getAccessToken();
    }

    @GetMapping(path = "/payment", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object testPay(){
        SmsPaymentRequestDto smsPaymentRequestDto = new SmsPaymentRequestDto(43l);
        return webClientNCCoreService.makePayment(smsPaymentRequestDto);
    }

}
