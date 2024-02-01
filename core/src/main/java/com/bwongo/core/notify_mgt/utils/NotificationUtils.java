package com.bwongo.core.notify_mgt.utils;

import com.bwongo.core.base.model.enums.StudentStatus;
import com.bwongo.core.notify_mgt.model.dto.NotificationDriverDto;
import com.bwongo.core.notify_mgt.model.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 11/12/23
 **/
public class NotificationUtils {

    @Value("${notification.sms.pick-up}")
    private String pickUpSms;

    @Value("${notification.sms.drop-off}")
    private String dropOffSms;

    @Value("${notification.sms.on-school}")
    private String onSchoolSms;

    private NotificationUtils() {  }

    public static NotificationDto mapNotificationDriverDtoToNotificationDto(NotificationDriverDto notificationDriverDto){
        return new NotificationDto(
                notificationDriverDto.studentUsername(),
                notificationDriverDto.studentStatus(),
                notificationDriverDto.appRef(),
                notificationDriverDto.latitudeCoordinate(),
                notificationDriverDto.longitudeCoordinate()
        );
    }

    public static String trimSchoolName(String schoolName){
        if(schoolName.length() > 60){
            return schoolName.substring(0, 58) + "...";
        }else{
            return schoolName;
        }
    }
}
