package com.nc.sms_notification.api;

import com.nc.sms_notification.service.SmsService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping(path = "/test/{number}/message/{message}")
    public Object testSms(@PathVariable("number") String number,
                          @PathVariable("message") String message){
        return smsService.testSms(number, message);
    }

}
