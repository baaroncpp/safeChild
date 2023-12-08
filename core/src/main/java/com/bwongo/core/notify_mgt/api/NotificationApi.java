package com.bwongo.core.notify_mgt.api;

import com.bwongo.core.notify_mgt.model.dto.BulkSignInRequestDto;
import com.bwongo.core.notify_mgt.model.dto.BulkSignInResponse;
import com.bwongo.core.notify_mgt.model.dto.NotificationDriverDto;
import com.bwongo.core.notify_mgt.model.dto.NotificationDto;
import com.bwongo.core.notify_mgt.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 11/11/23
 **/
@Tag(name = "Notifications",description = "School staff and drivers send student scan notifications")
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class NotificationApi {

    private final NotificationService notificationService;

    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.WRITE','ADMIN_ROLE.WRITE')")
    @PostMapping(path = "send/driver/notification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object sendNotificationDriver(@RequestBody NotificationDriverDto notificationDriverDto) {
        return notificationService.sendNotificationDriver(notificationDriverDto);
    }

    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.WRITE','ADMIN_ROLE.WRITE')")
    @PostMapping(path = "send/staff/notification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object sendNotificationStaff(@RequestBody NotificationDto notificationDto) {
        return notificationService.sendNotificationStaff(notificationDto, Boolean.FALSE);
    }

    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.WRITE','ADMIN_ROLE.WRITE')")
    @PostMapping(path = "bulk/school-sign-in/trip", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BulkSignInResponse sendBulkNotification(@RequestBody BulkSignInRequestDto bulkSignInRequestDto) {
        return notificationService.bulkOnSchool(bulkSignInRequestDto);
    }
}
