package com.nc.sms_notification.jobs;

import com.nc.sms_notification.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/3/24
 * @LocalTime 1:14 PM
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduledEvents {

    private final SmsService smsService;

    @Scheduled(fixedDelay = 60000, initialDelay = 30000)
    public void disburseLoan(){
        try {
            log.info("Sending pending notifications");
            smsService.sendPendingNotifications();
        }catch (Exception e){
            log.error(e.getMessage());
            log.error("Failed to send pending notifications", e);
        }
    }
}
