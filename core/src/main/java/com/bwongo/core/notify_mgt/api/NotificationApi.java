package com.bwongo.core.notify_mgt.api;

import com.bwongo.core.notify_mgt.model.dto.NotificationDriverDto;
import com.bwongo.core.notify_mgt.model.dto.NotificationDto;
import com.bwongo.core.notify_mgt.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 11/11/23
 **/
@RequiredArgsConstructor
public class NotificationApi {

    private final NotificationService notificationService;

    @PostMapping(path = "send/driver/notification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object sendNotificationDriver(@RequestBody NotificationDriverDto notificationDriverDto) {
        return notificationService.sendNotificationDriver(notificationDriverDto);
    }

    @PostMapping(path = "send/staff/notification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object sendNotificationStaff(@RequestBody NotificationDto notificationDto) {
        return notificationService.sendNotificationStaff(notificationDto, Boolean.FALSE);
    }

    @GetMapping(path = "bulk/school-sign-in/trip/{id}")
    public Object sendBulkNotification(@PathVariable("id") Long id) {
        return notificationService.bulkOnSchool(id);
    }
}
