package com.nc.safechild.network;

import com.nc.safechild.notification.model.jpa.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
public class MessageBrokerService {

    @Value("${notification.exchange.name}")
    private String exchange;

    @Value("${notification.routing.key.sms}")
    private String smsRoutingKey;

    private final RabbitTemplate rabbitTemplate;

    public void sendSms(Notification notification){
        rabbitTemplate.convertAndSend(exchange, smsRoutingKey, notification);
    }
}
