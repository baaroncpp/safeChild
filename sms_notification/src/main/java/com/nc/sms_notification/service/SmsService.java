package com.nc.sms_notification.service;

import com.nc.sms_notification.model.dto.SmsPaymentRequestDto;
import com.nc.sms_notification.model.dto.SmsPaymentResponseDto;
import com.nc.sms_notification.model.enums.SmsStatus;
import com.nc.sms_notification.model.jpa.Notification;
import com.nc.sms_notification.network.WebClientNCCoreService;
import com.nc.sms_notification.network.WebClientSmsService;
import com.nc.sms_notification.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

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
    private final WebClientSmsService webClientSmsService;
    private final WebClientNCCoreService webClientNCCoreService;

    public void sendSmsByMessageQueue(Notification notify){
        log.info("updated");
        var existingNotification = notificationRepository.findById(notify.getId());

        if(existingNotification.isPresent()){
            
            sendSms(existingNotification.get());
            /*var notification = existingNotification.get();

            var smsPaymentRequestDto = new SmsPaymentRequestDto(notification.getId());

            SmsPaymentResponseDto smsPaymentResponseDto = null;
            try {
                smsPaymentResponseDto = webClientNCCoreService.makePayment(smsPaymentRequestDto);
            }catch (Exception ex){
                log.error(ex.getMessage());
            }

            if(smsPaymentResponseDto != null &&
                    !notification.getStatus().equals(SmsStatus.SUCCESS) &&
                    smsPaymentResponseDto.coreBankingStatus().contains("PROCESSED")){

                log.info("Core Banking status: " + smsPaymentResponseDto.coreBankingStatus());

                var smsResponse = webClientSmsService.makeSmsCall(notification.getReceiver(), notification.getMessage());
                log.info(smsResponse);

                JSONObject jsonObject = new JSONObject(smsResponse);
                var success = jsonObject.getBoolean("success");

                if(success){
                    var statusMessage = jsonObject.getJSONArray("messages").getString(0);
                    notification.setStatus(SmsStatus.SUCCESS);
                    notification.setStatusNote(statusMessage);
                    log.info("Success ........ "+statusMessage);
                    
                }else{
                    var errorMessage = jsonObject.getJSONArray("messages").getString(0);
                    log.error("Failed ........ "+errorMessage);

                    notification.setStatus(SmsStatus.FAILED);
                    notification.setStatusNote(errorMessage);
                }

                notificationRepository.save(notification);
                log.info("notification updated");
            }
*/
        }else{
            log.error("Sms not found");
        }
    }

    public Object testSms(String number, String message){
        return webClientSmsService.makeSmsCall(number, message);
    }

    @Transactional
    public void sendPendingNotifications() {
        var notifications = notificationRepository.findAllByStatus(SmsStatus.PENDING);

        for(Notification notification : notifications){
            try {
                sendSms(notification);
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }

    private void sendSms(Notification notification){
        var smsPaymentRequestDto = new SmsPaymentRequestDto(notification.getId());

        SmsPaymentResponseDto smsPaymentResponseDto = null;
        try {
            smsPaymentResponseDto = webClientNCCoreService.makePayment(smsPaymentRequestDto);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }

        if(smsPaymentResponseDto != null &&
                !notification.getStatus().equals(SmsStatus.SUCCESS) &&
                smsPaymentResponseDto.coreBankingStatus().contains("PROCESSED")){

            log.info("Core Banking status: " + smsPaymentResponseDto.coreBankingStatus());

            var smsResponse = webClientSmsService.makeSmsCall(notification.getReceiver(), notification.getMessage());
            log.info(smsResponse);

            JSONObject jsonObject = new JSONObject(smsResponse);
            var success = jsonObject.getBoolean("success");

            if(success){
                var statusMessage = jsonObject.getJSONArray("messages").getString(0);
                notification.setStatus(SmsStatus.SUCCESS);
                notification.setStatusNote(statusMessage);
                notification.setTransactionId(smsPaymentResponseDto.transactionReference());
                log.info("Success ........ "+statusMessage);

            }else{
                var errorMessage = jsonObject.getJSONArray("messages").getString(0);
                log.error("Failed ........ "+errorMessage);

                notification.setStatus(SmsStatus.FAILED);
                notification.setStatusNote(errorMessage);
            }

            notificationRepository.save(notification);
            log.info("notification updated");
        }
    }
}
