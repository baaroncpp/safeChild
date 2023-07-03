package com.nc.sms_notification.service;

import com.nc.sms_notification.exceptions.model.ExceptionType;
import com.nc.sms_notification.model.enums.SmsStatus;
import com.nc.sms_notification.model.jpa.Notification;
import com.nc.sms_notification.network.WebClientService;
import com.nc.sms_notification.repository.NotificationRepository;
import com.nc.sms_notification.utils.Validate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import static com.nc.sms_notification.utils.ConstantMessages.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 6/29/23
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {

    private final NotificationRepository notificationRepository;
    private final WebClientService webClientService;

    public void sendSms(Notification notify){

        var existingNotification = notificationRepository.findById(notify.getId());
        Validate.isPresent(existingNotification, NOTIFICATION_NOT_FOUND, notify.getId());
        var notification = existingNotification.get();

        var smsResponse = webClientService.makeSmsCall(notification.getReceiver(), notification.getMessage());

        JSONObject jsonObject = new JSONObject(smsResponse);
        var success = jsonObject.getBoolean("success");

        if(!success){
            var errorMessage = jsonObject.getString("error_message");
            log.error(errorMessage);

            notification.setStatus(SmsStatus.FAILED);
            notification.setStatusNote(errorMessage);
        }
        var statusMessage = jsonObject.getString("message");
        notification.setStatus(SmsStatus.SUCCESS);
        notification.setStatusNote(statusMessage);

        log.info(statusMessage);

        notificationRepository.save(notification);

        Validate.isTrue(success, ExceptionType.BAD_REQUEST, SMS_FAILED, notification.getTransactionId());
    }

    public Object testSms(String number, String message){
        return webClientService.makeSmsCall(number, message);
    }
}
