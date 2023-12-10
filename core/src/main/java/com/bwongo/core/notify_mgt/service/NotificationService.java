package com.bwongo.core.notify_mgt.service;

import com.bwongo.commons.models.exceptions.BadRequestException;
import com.bwongo.commons.models.utils.DateTimeUtil;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.core.base.model.enums.*;
import com.bwongo.core.base.model.jpa.TLocation;
import com.bwongo.core.base.repository.TLocationRepository;
import com.bwongo.core.base.service.AuditService;
import com.bwongo.core.notify_mgt.model.dto.*;
import com.bwongo.core.notify_mgt.model.jpa.Notification;
import com.bwongo.core.notify_mgt.network.MessageBrokerService;
import com.bwongo.core.notify_mgt.repository.NotificationRepository;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.school_mgt.model.jpa.TSchoolUser;
import com.bwongo.core.school_mgt.repository.SchoolUserRepository;
import com.bwongo.core.student_mgt.model.dto.StudentDayResponseDto;
import com.bwongo.core.student_mgt.model.jpa.StudentDay;
import com.bwongo.core.student_mgt.model.jpa.StudentTravel;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import com.bwongo.core.student_mgt.repository.*;
import com.bwongo.core.student_mgt.service.StudentDtoService;
import com.bwongo.core.trip_mgt.model.jpa.Trip;
import com.bwongo.core.trip_mgt.repository.TripRepository;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import com.bwongo.core.user_mgt.repository.TUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.bwongo.commons.models.utils.DateTimeUtil.*;
import static com.bwongo.core.base.utils.BasicMsgConstants.DATE_TIME_FORMAT;
import static com.bwongo.core.notify_mgt.utils.NotificationMsgConstants.*;
import static com.bwongo.core.notify_mgt.utils.NotificationUtils.*;
import static com.bwongo.core.student_mgt.utils.StudentMsgConstant.*;
import static com.bwongo.core.trip_mgt.utils.TripMsgConstants.*;
import static com.bwongo.core.trip_mgt.utils.TripUtils.getRemainingStudentsOnTrip;
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
    private final TUserRepository tUserRepository;

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
    private static final String SUCCESS = "SUCCESS";
    private static final String SUCCESS_NOTE = "Successfully signed in all students on trip";
    private static final String FAILED_NOTIFICATION = "Failed to send notification";

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
    private final StudentDtoService studentDtoService;

    private TStudent getStudentByUsername(String username){
        var existingStudent = studentRepository.findByStudentUsername(username);
        Validate.isPresent(this, existingStudent, STUDENT_NOT_FOUND_USERNAME, username);
        return existingStudent.get();
    }

    @Transactional
    public NotificationResponseDto sendNotificationDriver(NotificationDriverDto notificationDriverDto) {

        notificationDriverDto.validate();
        var studentUsername = notificationDriverDto.studentUsername();
        var studentStatus = StudentStatus.valueOf(notificationDriverDto.studentStatus());
        var student = getStudentByUsername(studentUsername);
        var currentDay = getCurrentOnlyDate(this);

        var staff = new TUser();
        staff.setId(auditService.getLoggedInUser().getId());

        var existingSchoolUser = schoolUserRepository.findByUser(staff);
        Validate.isPresent(this, existingSchoolUser, SCHOOL_USER_NOT_FOUND, staff.getId());

        var schoolUser = new TSchoolUser();
        if(existingSchoolUser.isPresent())
            schoolUser = existingSchoolUser.get();

        //Update staff object
        staff = schoolUser.getUser();

        var school = schoolUser.getSchool();

        var existingTrip = tripRepository.findById(notificationDriverDto.tripId());
        Validate.isPresent(this, existingTrip, TRIP_NOT_FOUND, notificationDriverDto.tripId());

        var trip = new Trip();
        if(existingTrip.isPresent())
            trip = existingTrip.get();

        Validate.isTrue(this, !trip.getTripStatus().equals(TripStatus.ENDED), ExceptionType.BAD_REQUEST, TRIP_ENDED);

        var staffUsername = schoolUser.getUser().getUsername();

        Validate.isTrue(this, Objects.equals(student.getSchool().getId(), schoolUser.getSchool().getId()), ExceptionType.BAD_REQUEST, STUDENT_STAFF_NOT_SAME_SCHOOL, staffUsername, studentUsername);
        checkIfStudentDayStatusExists(student, studentStatus, currentDay);

        var location = new TLocation();
        location.setLatitude(notificationDriverDto.latitudeCoordinate());
        location.setLongitude(notificationDriverDto.longitudeCoordinate());
        auditService.stampLongEntity(location);
        locationRepository.save(location);

        var guardianPhoneNumbers = studentGuardianRepository.findAllByStudent(student).stream()
                .filter(studentGuardian -> studentGuardian.getGuardian().isNotified())
                .map(studentGuardian -> studentGuardian.getGuardian().getPhoneNumber())
                .toList();

        switch(studentStatus) {
            case HOME_PICK_UP:
                Validate.isTrue(this, trip.getTripType().equals(TripType.PICK_UP), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS_FOR_TRIP, studentUsername);
                Validate.isTrue(this, studentDayRepository.findBySchoolDateAndStudentAndSchool(currentDay, student, school).isEmpty(),
                        ExceptionType.BAD_REQUEST,
                        STUDENT_HAS_ALREADY_BEEN_IN_STATUS, studentUsername, studentStatus);

                return sendNotification(location, student, school, trip, staff, studentStatus, guardianPhoneNumbers);

            case SCHOOL_SIGN_IN:
                Validate.isTrue(this, trip.getTripType().equals(TripType.PICK_UP), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS_FOR_TRIP, studentUsername);
                var studentTravelSignIn = studentTravelRepository.findByStudentAndTripAndStudentStatus(student, trip, StudentStatus.HOME_PICK_UP);
                Validate.isPresent(this, studentTravelSignIn, STUDENT_NOT_PICKED_UP, student.getStudentUsername());

                return sendNotification(location, student, school, trip, staff, studentStatus, guardianPhoneNumbers);

            case SCHOOL_SIGN_OUT:
                Validate.isTrue(this, trip.getTripType().equals(TripType.DROP_OFF), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS_FOR_TRIP, studentUsername);

                return sendNotification(location, student, school, trip, staff, studentStatus, guardianPhoneNumbers);

            case HOME_DROP_OFF:
                Validate.isTrue(this, trip.getTripType().equals(TripType.DROP_OFF), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS_FOR_TRIP, studentUsername);
                Validate.isTrue(this, studentTravelRepository.findByStudentAndTripAndStudentStatus(student, trip, studentStatus).isPresent(),
                        ExceptionType.BAD_REQUEST, STUDENT_NOT_ON_TRIP, studentUsername);

                return sendNotification(location, student, school, trip, staff, studentStatus, guardianPhoneNumbers);
            default:
                throw new BadRequestException(this, FAILED_NOTIFICATION);
        }
    }

    @Transactional
    public NotificationResponseDto sendNotificationStaff(NotificationDto notificationDto, Boolean onTrip) {

        notificationDto.validate();
        var studentUsername = notificationDto.studentUsername();
        var stringStudentStatus = notificationDto.studentStatus();

        Validate.isTrue(this, (StudentStatus.SCHOOL_SIGN_IN.name().equals(stringStudentStatus) || StudentStatus.SCHOOL_SIGN_OUT.name().equals(stringStudentStatus)),
                ExceptionType.ACCESS_DENIED,
                STUDENT_STATUS_NOT_ALLOWED);

        var student = getStudentByUsername(studentUsername);

        var staff = new TUser();
        staff.setId(auditService.getLoggedInUser().getId());

        var existingSchoolUser = schoolUserRepository.findByUser(staff);
        Validate.isPresent(this, existingSchoolUser, SCHOOL_USER_NOT_FOUND, staff.getId());
        var schoolUser = existingSchoolUser.get();

        var staffUsername = schoolUser.getUser().getUsername();

        Validate.isTrue(this, Objects.equals(schoolUser.getSchool().getId(), student.getSchool().getId()), ExceptionType.BAD_REQUEST, STUDENT_STAFF_NOT_SAME_SCHOOL, studentUsername, staffUsername);

        checkIfStudentDayStatusExists(student, StudentStatus.valueOf(stringStudentStatus), getCurrentOnlyDate(this));

        var location = new TLocation();
        location.setLatitude(notificationDto.latitudeCoordinate());
        location.setLongitude(notificationDto.longitudeCoordinate());

        auditService.stampLongEntity(location);
        locationRepository.save(location);

        var studentDay = new StudentDay();
        studentDay.setStaff(schoolUser.getUser());
        studentDay.setStudent(student);
        studentDay.setSchool(studentDay.getSchool());
        studentDay.setSchoolDate(getCurrentOnlyDate(this));
        studentDay.setOnTrip(onTrip);
        studentDay.setLocation(location);

        auditService.stampAuditedEntity(studentDay);
        studentDayRepository.save(studentDay);

        //get notified guardians
        var guardianPhoneNumbers = studentGuardianRepository.findAllByStudent(student).stream()
                .filter(studentGuardian -> studentGuardian.getGuardian().isNotified())
                .map(studentGuardian -> studentGuardian.getGuardian().getPhoneNumber())
                .toList();

        return sendSms(guardianPhoneNumbers,
                StudentStatus.valueOf(stringStudentStatus),
                student.getStudentUsername(),
                staff.getUsername(),
                staff.getUserType(),
                schoolUser.getSchool().getSchoolName());
    }

    @Transactional
    public BulkSignInResponse bulkOnSchool(BulkSignInRequestDto bulkSignInRequestDto) {

        bulkSignInRequestDto.validate();
        Long tripId = bulkSignInRequestDto.tripId();

        var existingTrip = tripRepository.findById(tripId);
        Validate.isPresent(this, existingTrip, TRIP_NOT_FOUND, tripId);

        var trip = new Trip();
        if(existingTrip.isPresent())
            trip = existingTrip.get();

        Validate.isTrue(this, trip.getTripType().equals(TripType.PICK_UP), ExceptionType.BAD_REQUEST, BULK_ONLY_PICK_UP);
        Validate.isTrue(this, !trip.getTripStatus().equals(TripStatus.ENDED), ExceptionType.BAD_REQUEST, TRIP_ENDED);

        var studentTravelList = getStudentsStillOnTrip(trip);

        for(StudentTravel obj : studentTravelList){
            var notificationDriverDto = new NotificationDriverDto(
                    tripId,
                    obj.getStudent().getStudentUsername(),
                    StudentStatus.SCHOOL_SIGN_IN.name(),
                    ("NC"+ UUID.randomUUID().toString().replace("-","")).substring(0, 10),
                    bulkSignInRequestDto.latitudeCoordinate(),
                    bulkSignInRequestDto.longitudeCoordinate()
            );
            sendNotificationDriver(notificationDriverDto);
        }
        trip.setTripStatus(TripStatus.ENDED);
        trip.setModifiedOn(DateTimeUtil.getCurrentUTCTime());
        tripRepository.save(trip);

        return new BulkSignInResponse(
                trip,
                SUCCESS,
                SUCCESS_NOTE
        );
    }

    public List<StudentDayResponseDto> getStudentDayByStaffAndDate(String stringDate, Pageable pageable){

        var date = stringToDate(stringDate, DATE_TIME_FORMAT);
        var staffId = auditService.getLoggedInUser().getId();

        var existingStaff = tUserRepository.findById(staffId);
        var staff = existingStaff.get();

        var existingSchoolStaff = schoolUserRepository.findByUser(staff);
        Validate.isPresent(this, existingSchoolStaff, SCHOOL_USER_NOT_FOUND, staffId);

        return studentDayRepository.findAllByStaffAndSchoolDate(staff, date, pageable).stream()
                .map(studentDtoService::studentDayToDto)
                .toList();
    }

    private NotificationResponseDto sendNotification(TLocation location, TStudent student, TSchool school, Trip trip, TUser staff, StudentStatus studentStatus, List<String> guardianPhoneNumbers){

        var studentTravel = new StudentTravel();
        var studentDay = new StudentDay();

        studentDay.setStaff(staff);
        studentDay.setStudent(student);
        studentDay.setSchool(school);
        studentDay.setSchoolDate(getCurrentOnlyDate(this));
        studentDay.setOnTrip(Boolean.FALSE);
        studentDay.setLocation(location);

        auditService.stampAuditedEntity(studentDay);
        studentDayRepository.save(studentDay);

        studentTravel.setStudent(student);
        studentTravel.setTrip(trip);
        studentTravel.setStudentStatus(studentStatus);
        studentTravel.setSchool(school);
        studentTravel.setLocation(location);

        auditService.stampAuditedEntity(studentTravel);
        studentTravelRepository.save(studentTravel);

        return sendSms(guardianPhoneNumbers,
                studentStatus,
                student.getStudentUsername(),
                staff.getUsername(),
                staff.getUserType(),
                school.getSchoolName());
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

        Validate.isTrue(this, existingStudentDay.isEmpty(), ExceptionType.BAD_REQUEST, STUDENT_HAS_ALREADY_BEEN_IN_STATUS, student.getStudentUsername(), studentStatus);

        //TODO must be revised if students that did not check in should not be allowed to check out
        if (studentStatus.equals(StudentStatus.SCHOOL_SIGN_OUT)) {
            var studentDays = studentDayRepository.findBySchoolDateAndStudent(schoolDate, student);
            Validate.isTrue(this, !studentDays.isEmpty(), ExceptionType.BAD_REQUEST, STUDENT_NOT_CHECKED_IN, student.getStudentUsername());
        }
    }

    private List<StudentTravel> getStudentsStillOnTrip(Trip trip){

        var studentTravelPickUpList = getStudentTravelsByTripAndStudentStatus(trip, StudentStatus.HOME_PICK_UP);
        var studentTravelSignInList = getStudentTravelsByTripAndStudentStatus(trip, StudentStatus.SCHOOL_SIGN_IN);
        return getRemainingStudentsOnTrip(studentTravelPickUpList, studentTravelSignInList);
    }

    private List<StudentTravel> getStudentTravelsByTripAndStudentStatus(Trip trip, StudentStatus studentStatus){
        return studentTravelRepository.findAllByTripAndStudentStatus(trip, studentStatus);
    }

    private String getSms(StudentStatus status,
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
