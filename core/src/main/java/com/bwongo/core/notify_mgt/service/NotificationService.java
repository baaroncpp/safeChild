package com.bwongo.core.notify_mgt.service;

import com.bwongo.commons.models.utils.Validate;
import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.core.base.model.enums.*;
import com.bwongo.core.base.model.jpa.TLocation;
import com.bwongo.core.base.repository.TLocationRepository;
import com.bwongo.core.base.service.AuditService;
import com.bwongo.core.notify_mgt.model.dto.NotificationDriverDto;
import com.bwongo.core.notify_mgt.model.dto.NotificationDto;
import com.bwongo.core.notify_mgt.model.dto.NotificationResponseDto;
import com.bwongo.core.notify_mgt.model.jpa.Notification;
import com.bwongo.core.notify_mgt.network.MessageBrokerService;
import com.bwongo.core.notify_mgt.repository.NotificationRepository;
import com.bwongo.core.school_mgt.repository.SchoolUserRepository;
import com.bwongo.core.student_mgt.model.jpa.StudentDay;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import com.bwongo.core.student_mgt.repository.*;
import com.bwongo.core.trip_mgt.repository.TripRepository;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.bwongo.commons.models.utils.DateTimeUtil.*;
import static com.bwongo.core.notify_mgt.utils.NotificationMsgConstants.*;
import static com.bwongo.core.notify_mgt.utils.NotificationUtils.*;
import static com.bwongo.core.student_mgt.utils.StudentMsgConstant.*;
import static com.bwongo.core.trip_mgt.utils.TripMsgConstants.TRIP_ENDED;
import static com.bwongo.core.trip_mgt.utils.TripMsgConstants.TRIP_NOT_FOUND;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.SCHOOL_USER_NOT_FOUND;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 11/11/23
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    @Value("${notification.sms.sender}")
    private String smsSender;

    @Value("${notification.sms.pick-up}")
    private String pickUpSms;

    @Value("${notification.sms.drop-off}")
    private String dropOffSms;

    @Value("${notification.sms.on-school}")
    private String onSchoolSms;

    @Value("${notification.sms.off-school}")
    private String offSchoolSms;

    private final StudentDayRepository studentDayRepository;
    private final StudentTravelRepository studentTravelRepository;
    private final StudentRepository studentRepository;
    private final AuditService auditService;
    private final SchoolUserRepository schoolUserRepository;
    private final TLocationRepository locationRepository;
    private final NotificationRepository notificationRepository;
    private final StudentGuardianRepository studentGuardianRepository;
    private final MessageBrokerService messageBrokerService;
    private final TripRepository tripRepository;

    private TStudent getStudentByUsername(String username){
        var existingStudent = studentRepository.findByStudentUsername(username);
        Validate.isPresent(existingStudent, STUDENT_NOT_FOUND_USERNAME, username);
        return existingStudent.get();
    }

    @Transactional
    public NotificationResponseDto sendNotificationDriver(NotificationDriverDto notificationDriverDto) {

        notificationDriverDto.validate();
        var studentUsername = notificationDriverDto.studentUsername();
        var stringStudentStatus = notificationDriverDto.studentStatus();
        var studentStatus = StudentStatus.valueOf(stringStudentStatus);
        var notificationDto = mapNotificationDriverDtoToNotificationDto(notificationDriverDto);

        var existingTrip = tripRepository.findById(notificationDriverDto.tripId());
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, notificationDriverDto.tripId());
        var trip = existingTrip.get();

        Validate.isTrue(!trip.getTripStatus().equals(TripStatus.ENDED), ExceptionType.BAD_REQUEST, TRIP_ENDED);

        var student = getStudentByUsername(studentUsername);

        var staff = new TUser();
        staff.setId(auditService.getLoggedInUser().getId());

        var existingSchoolUser = schoolUserRepository.findByUser(staff);
        Validate.isPresent(existingSchoolUser, SCHOOL_USER_NOT_FOUND, staff.getId());
        var schoolUser = existingSchoolUser.get();

        var staffUsername = schoolUser.getUser().getUsername();

        Validate.isTrue(Objects.equals(student.getSchool().getId(), schoolUser.getSchool().getId()), ExceptionType.BAD_REQUEST, STUDENT_STAFF_NOT_SAME_SCHOOL, staffUsername, studentUsername);

        var location = new TLocation();
        location.setLatitude(notificationDriverDto.latitudeCoordinate());
        location.setLongitude(notificationDriverDto.longitudeCoordinate());
        auditService.stampLongEntity(location);

        if(studentStatus.equals(StudentStatus.SCHOOL_SIGN_OUT))
            return sendNotificationStaff(notificationDto, Boolean.TRUE);

        if(studentStatus.equals(StudentStatus.SCHOOL_SIGN_IN))
            return sendNotificationStaff(notificationDto, Boolean.FALSE);


        if(StudentStatus.HOME_DROP_OFF.equals(studentStatus)){
            Validate.isTrue(trip.getTripType().equals(TripType.DROP_OFF), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS_FOR_TRIP, studentUsername);

            checkIfStudentDayStatusExists(student, studentStatus, getCurrentOnlyDate());
            var existingDropOffStudentDay = studentDayRepository.findBySchoolDateAndStudentAndStudentStatus(getCurrentOnlyDate(), student, studentStatus);
            Validate.isTrue(existingDropOffStudentDay.isEmpty(), ExceptionType.BAD_REQUEST, STUDENT_ALREADY_DROPPED_OFF, notificationDriverDto.studentUsername());

            var existingSignedOutStudentDay = studentDayRepository.findBySchoolDateAndStudentAndStudentStatus(getCurrentOnlyDate(), student, StudentStatus.SCHOOL_SIGN_OUT);
            Validate.isTrue(existingSignedOutStudentDay.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, STUDENT_WAS_NOT_SIGNED_OUT, notificationDriverDto.studentUsername());

            var existingStudentTravel = studentTravelRepository.findByStudentAndTripAndStudentStatus(student, trip, StudentStatus.SCHOOL_SIGN_OUT);
            Validate.isTrue(existingStudentTravel.isPresent(), ExceptionType.BAD_REQUEST, STUDENT_NOT_ON_TRIP, notificationDriverDto.studentUsername());

            /*var studentTravel = new StudentTravel();
            studentTravel.setStudent(notificationDriverDto.studentUsername());
            studentTravel.setTrip(trip);
            studentTravel.setCreatedOn(getCurrentUTCTime());
            studentTravel.setModifiedOn(getCurrentUTCTime());
            studentTravel.setStudentStatus(StudentStatus.HOME_DROP_OFF);
            studentTravel.setSchoolId(studentStaffDetails.getStudentSchoolId());
            studentTravel.setFullName(studentStaffDetails.getStudentUser().getName());
            studentTravel.setLocation(savedLocation);

            studentTravelRepository.save(studentTravel);

            studentDay.setStudentStatus(StudentStatus.HOME_DROP_OFF);
            studentDay.setOnTrip(Boolean.FALSE);
            studentDay.setCreatedOn(getCurrentUTCTime());
            studentDay.setModifiedOn(getCurrentOnlyDate());
            studentDay.setSchoolId(studentStaffDetails.getStudentSchoolId());
            studentDay.setStudentUsername(notificationDriverDto.studentUsername());
            studentDay.setStaffUsername(notificationDriverDto.performedByUsername());
            studentDay.setSchoolDate(getCurrentOnlyDate());
            studentDay.setFullName(studentStaffDetails.getStudentUser().getName());
            studentDay.setLocation(savedLocation);

            studentDayRepository.save(studentDay);

            //changeOpenTripToInProgress(trip);

            return sendSms(sendSmsDto);*/
        }

        return null;
    }

    @Transactional
    public NotificationResponseDto sendNotificationStaff(NotificationDto notificationDto, Boolean onTrip) {

        notificationDto.validate();
        var studentUsername = notificationDto.studentUsername();
        var stringStudentStatus = notificationDto.studentStatus();

        Validate.isTrue((StudentStatus.SCHOOL_SIGN_IN.name().equals(stringStudentStatus) || StudentStatus.SCHOOL_SIGN_OUT.name().equals(stringStudentStatus)),
                ExceptionType.ACCESS_DENIED,
                STUDENT_STATUS_NOT_ALLOWED);

        var student = getStudentByUsername(studentUsername);

        var staff = new TUser();
        staff.setId(auditService.getLoggedInUser().getId());

        var existingSchoolUser = schoolUserRepository.findByUser(staff);
        Validate.isPresent(existingSchoolUser, SCHOOL_USER_NOT_FOUND, staff.getId());
        var schoolUser = existingSchoolUser.get();

        checkIfStudentDayStatusExists(student, StudentStatus.valueOf(stringStudentStatus), getCurrentOnlyDate());

        var location = new TLocation();
        location.setLatitude(notificationDto.latitudeCoordinate());
        location.setLongitude(notificationDto.longitudeCoordinate());

        auditService.stampLongEntity(location);
        locationRepository.save(location);

        var studentDay = new StudentDay();
        studentDay.setStaff(schoolUser.getUser());
        studentDay.setStudent(student);
        studentDay.setSchool(studentDay.getSchool());
        studentDay.setSchoolDate(getCurrentOnlyDate());
        studentDay.setOnTrip(onTrip);
        studentDay.setLocation(location);

        auditService.stampAuditedEntity(studentDay);
        studentDayRepository.save(studentDay);

        //get notified guardians
        List<String> guardianPhoneNumbers = studentGuardianRepository.findAllByStudent(student).stream()
                .filter(studentGuardian -> studentGuardian.getGuardian().isNotified())
                .map(studentGuardian -> studentGuardian.getGuardian().getPhoneNumber())
                .collect(Collectors.toList());

        return sendSms(guardianPhoneNumbers,
                StudentStatus.valueOf(stringStudentStatus),
                student.getStudentUsername(),
                staff.getUsername(),
                staff.getUserType(),
                schoolUser.getSchool().getSchoolName());
    }

    public Object bulkOnSchool(Long id) {
        return null;
    }

    private NotificationResponseDto sendSms(List<String> guardianPhoneNumbers, StudentStatus studentStatus,
                         String studentUsername, String staffUsername,
                         UserTypeEnum userType, String schoolName){

        var message = getSms(studentStatus, studentUsername,
                trimSchoolName(schoolName), userType.getNote(), staffUsername);

        var notification = new Notification();
        notification.setMessage(message);
        notification.setSender(smsSender);
        notification.setReceivers(guardianPhoneNumbers);
        notification.setStatus(SmsStatus.PENDING);
        notification.setCreatedOn(getCurrentUTCTime());

        var notify = notificationRepository.save(notification);
        messageBrokerService.sendSms(notify);

        return new NotificationResponseDto(
                notify.getId(),
                studentStatus,
                schoolName
        );
    }

    private void checkIfStudentDayStatusExists(TStudent student, StudentStatus studentStatus, Date schoolDate) {
        var existingStudentDay = studentDayRepository.findBySchoolDateAndStudentAndStudentStatus(schoolDate,
                student,
                studentStatus);

        Validate.isTrue(existingStudentDay.isEmpty(), ExceptionType.BAD_REQUEST, STUDENT_HAS_ALREADY_BEEN_IN_STATUS, student.getStudentUsername(), studentStatus);

        if (studentStatus.equals(StudentStatus.SCHOOL_SIGN_OUT)) {
            var studentDays = studentDayRepository.findBySchoolDateAndStudent(schoolDate, student);
            Validate.isTrue(!studentDays.isEmpty(), ExceptionType.BAD_REQUEST, STUDENT_NOT_CHECKED_IN, student.getStudentUsername());
        }
    }

    public String getSms(StudentStatus status,
                                String studentUsername,
                                String schoolName,
                                String schoolUserType,
                                String staffUsername){

        var time = LocalTime.now();
        var formatter = DateTimeFormatter.ofPattern("HH:mm");

        if(status.equals(StudentStatus.HOME_PICK_UP))
            return String.format(pickUpSms, studentUsername, schoolUserType, staffUsername, time.format(formatter));

        if(status.equals(StudentStatus.HOME_DROP_OFF))
            return String.format(dropOffSms, studentUsername, schoolName, schoolUserType, staffUsername, time.format(formatter));

        if(status.equals(StudentStatus.SCHOOL_SIGN_OUT))
            return String.format(offSchoolSms, studentUsername, schoolUserType, staffUsername, schoolName, time.format(formatter));

        if(status.equals(StudentStatus.SCHOOL_SIGN_IN))
            return String.format(onSchoolSms, studentUsername, schoolName, schoolUserType, staffUsername, time.format(formatter));

        return null;
    }
}
