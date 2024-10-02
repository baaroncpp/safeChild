package com.bwongo.core.notify_mgt.service;

import com.bwongo.core.notify_mgt.model.dto.SmsNotificationResponseDto;
import com.bwongo.core.notify_mgt.model.jpa.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/2/24
 * @Time 9:29 AM
 **/
@Service
@Slf4j
public class NotificationDtoService {


    public SmsNotificationResponseDto notificationToSmsDto(Notification notification){

        if(notification == null)
            return null;

        return new SmsNotificationResponseDto(
                notification.getId(),
                notification.getCreatedOn(),
                notification.getModifiedOn(),
                notification.getReceiver(),
                notification.getSender(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getStatusNote(),
                notification.getTransactionId(),
                notification.getExternalTransactionId(),
                notification.getAccountNumber()
        );
    }
}
