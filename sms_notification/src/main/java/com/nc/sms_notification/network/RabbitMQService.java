package com.nc.sms_notification.network;

import com.nc.sms_notification.model.jpa.Notification;
import com.nc.sms_notification.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 6/29/23
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQService {

    private final SmsService smsService;

    @Value("${notification.queue.name.sms}")
    private String smsQueue;

    @RabbitListener(queues = "sms.queue")
    public void receiveSms(Notification notification){
        log.info("received sms from CORE");
        smsService.sendSms(notification);
    }
}
