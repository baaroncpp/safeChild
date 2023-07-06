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
        log.info("updated");
        var existingNotification = notificationRepository.findById(notify.getId());

        if(existingNotification.isPresent()){
            var notification = existingNotification.get();

            if(!notification.getStatus().equals(SmsStatus.SUCCESS)){
                var smsResponse = webClientService.makeSmsCall(notification.getReceiver(), notification.getMessage());
                log.info("sms sent "+ notification.getMessage());

                JSONObject jsonObject = new JSONObject(smsResponse);
                var success = jsonObject.getBoolean("success");

                if(success){
                    var statusMessage = jsonObject.getString("message");
                    notification.setStatus(SmsStatus.SUCCESS);
                    notification.setStatusNote(statusMessage);
                    log.info(statusMessage);
                    
                }else{
                    var errorMessage = jsonObject.getString("message");
                    log.error(errorMessage);

                    notification.setStatus(SmsStatus.FAILED);
                    notification.setStatusNote(errorMessage);
                }

                notificationRepository.save(notification);
                log.info("notification updated");
            }

        }else{
            log.error("Sms not found");
        }
    }

    public Object testSms(String number, String message){
        return webClientService.makeSmsCall(number, message);
    }
}
