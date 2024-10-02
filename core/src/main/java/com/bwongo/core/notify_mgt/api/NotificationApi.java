package com.bwongo.core.notify_mgt.api;

import com.bwongo.core.base.model.dto.PageResponseDto;
import com.bwongo.core.notify_mgt.model.dto.*;
import com.bwongo.core.notify_mgt.service.MailService;
import com.bwongo.core.notify_mgt.service.NotificationService;
import com.bwongo.core.student_mgt.model.dto.StudentDayResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

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
    private final MailService mailService;

    @GetMapping(path = "not/test")
    public void testEmail() throws MessagingException, UnsupportedEncodingException {
        mailService.sendEmail();
    }

    @Operation(summary = "Send or create student notification by a driver")
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

    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "notification/student-day/date", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudentDayResponseDto> getStudentDayByDate(@RequestParam("page") int page,
                                                           @RequestParam("size") int size,
                                                           @RequestParam("date") String date){
        var pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return notificationService.getStudentDayByStaffAndDate(date, pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE.READ', 'SCHOOL_ROLE.READ')")
    @GetMapping(path = "sms/notification/school", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseDto getNotificationsBySchool(@RequestParam(name = "page") int page,
                                                    @RequestParam(name = "size") int size,
                                                    @RequestParam(name = "startDate") String startDate,
                                                    @RequestParam(name = "endDate") String endDate,
                                                    @RequestParam(name = "schoolId") Long schoolId){
        var pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return notificationService.getNotifications(pageable, startDate, endDate, schoolId);
    }

    @PreAuthorize("hasAnyAuthority('MOBILE_APP_ROLE.WRITE','ADMIN_ROLE.WRITE', 'SCHOOL_ROLE.READ')")
    @PostMapping(path = "send/sms", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SmsNotificationResponseDto sendSmsNotification(@RequestBody SmsNotificationRequestDto smsNotificationRequestDto){
        return notificationService.sendSmsNotification(smsNotificationRequestDto);
    }
}
