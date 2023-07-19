package com.nc.safechild.student.api;

import com.nc.safechild.network.MessageBrokerService;
import com.nc.safechild.student.model.dto.AuthenticationDto;
import com.nc.safechild.student.model.dto.NotificationDriverDto;
import com.nc.safechild.student.model.dto.NotificationDto;
import com.nc.safechild.student.model.jpa.Notification;
import com.nc.safechild.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/26/23
 **/
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class StudentApi {

    private final StudentService studentService;
    private final MessageBrokerService messageBrokerService;

    @GetMapping(path = "student/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getUserByUsername(@PathVariable("username") String username){
        return studentService.getMemberByUsername(username);
    }

    @PostMapping(path = "login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object checkCredentials(@RequestBody AuthenticationDto authenticationDto) throws Exception {
        return studentService.checkCredentials(authenticationDto);
    }

    @GetMapping(path = "balance/account/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getAccountBalance(@PathVariable("number") String number){
        return studentService.getAccountBalance(number);
    }

    @GetMapping(path = "groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getGroups(){
        return studentService.getGroups();
    }

    /*@PostMapping(path = "send/notification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object sendNotification(@RequestBody NotificationDto notificationDto) {
        return studentService.sendNotification(notificationDto);
    }*/

    @PostMapping(path = "send/driver/notification", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object sendNotificationDriver(@RequestBody NotificationDriverDto notificationDriverDto) {
        return studentService.sendNotificationDriver(notificationDriverDto);
    }

    @GetMapping(path = "bulk/school-sign-in/trip/{id}")
    public Object sendBulkNotification(@PathVariable("id") Long id) {
        return studentService.bulkOnSchool(id);
    }

    @GetMapping(path = "events/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getEvents(@PathVariable("username") String username){
        return studentService.getDailyEventCount(username);
    }

    @GetMapping(path = "images/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getImages(@PathVariable("username") String username){
        return studentService.getProfileUrl(username);
    }

    @GetMapping(path = "test")
    public void test(){
        Notification notification = new Notification();
        notification.setMessage("testing");
        notification.setId(1L);
        messageBrokerService.sendSms(notification);
    }
}
