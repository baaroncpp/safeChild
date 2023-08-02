package com.nc.safechild.student.service;

import com.nc.safechild.base.utils.DateTimeUtil;
import com.nc.safechild.base.utils.Validate;
import com.nc.safechild.base.utils.WebServiceUtil;
import com.nc.safechild.exceptions.BadRequestException;
import com.nc.safechild.exceptions.model.ExceptionType;
import com.nc.safechild.network.MessageBrokerService;
import com.nc.safechild.notification.model.enums.SmsStatus;
import com.nc.safechild.notification.model.jpa.Notification;
import com.nc.safechild.student.models.jpa.UserStudentStatusCount;
import com.nc.safechild.student.models.enums.UserType;
import com.nc.safechild.student.repository.NotificationRepository;
import com.nc.safechild.student.models.NotificationRole;
import com.nc.safechild.student.models.dto.*;
import com.nc.safechild.student.models.enums.StudentStatus;
import com.nc.safechild.student.models.jpa.StudentDay;
import com.nc.safechild.student.models.jpa.StudentTravel;
import com.nc.safechild.student.repository.StudentDayRepository;
import com.nc.safechild.student.repository.StudentTravelRepository;
import com.nc.safechild.student.repository.UserStudentStatusCountRepository;
import com.nc.safechild.trip.model.enums.TripStatus;
import com.nc.safechild.trip.model.enums.TripType;
import com.nc.safechild.trip.model.jpa.Trip;
import com.nc.safechild.trip.repository.TripRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.strohalm.cyclos.webservices.access.CheckCredentialsParameters;
import nl.strohalm.cyclos.webservices.access.CredentialsStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.nc.safechild.base.utils.DateTimeUtil.getCurrentOnlyDate;
import static com.nc.safechild.base.utils.DateTimeUtil.getCurrentUTCTime;
import static com.nc.safechild.base.utils.MessageConstants.*;
import static com.nc.safechild.base.utils.WebServiceUtil.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/22/23
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {

    private static final String PRINCIPLE_TYPE = "USER";

    @Value("${host.ip}")
    private String hostIp;

    @Value("${transfer-type.sms-notification}")
    private Long smsPaymentTransferType;

    @Value("${accounts.sms}")
    private String mainSmsAccount;

    @Value("${notification.sms.pick-up}")
    private String pickUpSms;

    @Value("${notification.sms.drop-off}")
    private String dropOffSms;

    @Value("${notification.sms.on-school}")
    private String onSchoolSms;

    @Value("${notification.sms.off-school}")
    private String offSchoolSms;

    private static final String RECEIVER_PHONE = "receiver_phone";
    private static final String STUDENT_PHONE = "std_school";

    private final StudentTravelRepository studentTravelRepository;
    private final StudentDayRepository studentDayRepository;
    private final TripRepository tripRepository;
    private final NotificationRepository notificationRepository;
    private final MessageBrokerService messageBrokerService;
    private final UserStudentStatusCountRepository userStudentStatusCountRepository;

    @Transactional
    public NotificationResponseDto sendNotificationStaff(NotificationDto notificationDto, boolean isOnTrip){

        notificationDto.validate();
        Validate.isTrue((StudentStatus.SCHOOL_SIGN_IN.equals(StudentStatus.valueOf(notificationDto.studentStatus())) || StudentStatus.SCHOOL_SIGN_OUT.equals(StudentStatus.valueOf(notificationDto.studentStatus()))),
                ExceptionType.BAD_REQUEST,
                INVALID_STUDENT_STATUS_FOR_USER,
                StudentStatus.SCHOOL_SIGN_IN,
                StudentStatus.SCHOOL_SIGN_OUT);

        var studentStaffDetails = getStudentAndStaffDetails(notificationDto.studentUsername(), notificationDto.performedByUsername());

        var sendSmsDto = SendSmsDto.builder()
                .staffUserGroupId(studentStaffDetails.getStaffUser().getGroupId())
                .appRef(notificationDto.appRef())
                .schoolAccount(studentStaffDetails.getAccountNumber())
                .schoolName(studentStaffDetails.getSchoolName())
                .studentStatus(StudentStatus.valueOf(notificationDto.studentStatus()))
                .guardianPhoneNumber(studentStaffDetails.getGuardianPhoneNumber())
                .build();


        Validate.isTrue((studentStaffDetails.getStudentSchoolId()).equals(studentStaffDetails.getStaffSchoolId()), ExceptionType.BAD_REQUEST, STUDENT_AND_STUFF_NOT_SAME_SCHOOL);

        var studentDay = new StudentDay();

        if(StudentStatus.SCHOOL_SIGN_IN.equals(StudentStatus.valueOf(notificationDto.studentStatus()))) {

            var existingStudentDay = studentDayRepository.findBySchoolDateAndStudentUsernameAndStudentStatus(getCurrentOnlyDate(),
                    notificationDto.studentUsername(),
                    StudentStatus.valueOf(notificationDto.studentStatus()));

            Validate.isTrue(existingStudentDay.isEmpty(),
                    ExceptionType.BAD_REQUEST,
                    STUDENT_IS_ALREADY_SIGNED_IN,
                    notificationDto.studentUsername());

            studentDay.setStudentStatus(StudentStatus.SCHOOL_SIGN_IN);
            studentDay.setOnTrip(isOnTrip);
            studentDay.setCreatedOn(getCurrentUTCTime());
            studentDay.setModifiedOn(getCurrentOnlyDate());
            studentDay.setSchoolId(studentStaffDetails.getStudentSchoolId());
            studentDay.setStudentUsername(notificationDto.studentUsername());
            studentDay.setStaffUsername(notificationDto.performedByUsername());
            studentDay.setSchoolDate(getCurrentOnlyDate());

            studentDayRepository.save(studentDay);

            return sendSms(sendSmsDto);
        }

        if(StudentStatus.SCHOOL_SIGN_OUT.equals(StudentStatus.valueOf(notificationDto.studentStatus()))) {
            var inSchoolStudentDay = studentDayRepository.findBySchoolDateAndStudentUsernameAndStudentStatus(getCurrentOnlyDate(),
                    notificationDto.studentUsername(),
                    StudentStatus.SCHOOL_SIGN_IN);

            var outSchoolStudentDay = studentDayRepository.findBySchoolDateAndStudentUsernameAndStudentStatus(getCurrentOnlyDate(),
                    notificationDto.studentUsername(),
                    StudentStatus.SCHOOL_SIGN_OUT);

            Validate.isTrue(inSchoolStudentDay.isPresent(),
                    ExceptionType.RESOURCE_NOT_FOUND,
                    STUDENT_WAS_NOT_SIGNED_IN,
                    notificationDto.studentUsername());

            Validate.isTrue(outSchoolStudentDay.isEmpty(),
                    ExceptionType.BAD_REQUEST,
                    STUDENT_IS_ALREADY_SIGNED_OUT,
                    notificationDto.studentUsername());

            studentDay.setStudentStatus(StudentStatus.SCHOOL_SIGN_OUT);
            studentDay.setOnTrip(isOnTrip);
            studentDay.setCreatedOn(getCurrentUTCTime());
            studentDay.setModifiedOn(getCurrentOnlyDate());
            studentDay.setSchoolId(studentStaffDetails.getStudentSchoolId());
            studentDay.setStudentUsername(notificationDto.studentUsername());
            studentDay.setStaffUsername(notificationDto.performedByUsername());
            studentDay.setSchoolDate(getCurrentOnlyDate());

            studentDayRepository.save(studentDay);

            return sendSms(sendSmsDto);
        }
        throw new BadRequestException("Failed to send notification");
    }

    @Transactional
    public NotificationResponseDto sendNotificationDriver(NotificationDriverDto notificationDriverDto){

        notificationDriverDto.validate();

        var existingTrip = tripRepository.findById(notificationDriverDto.tripId());
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, notificationDriverDto.tripId());

        Trip trip = new Trip();
        if(existingTrip.isPresent())
            trip = existingTrip.get();

        Validate.isTrue(!trip.getTripStatus().equals(TripStatus.ENDED), ExceptionType.BAD_REQUEST, TRIP_ENDED);

        var studentStaffDetails = getStudentAndStaffDetails(notificationDriverDto.studentUsername(), notificationDriverDto.performedByUsername());

        var sendSmsDto = SendSmsDto.builder()
                .staffUserGroupId(studentStaffDetails.getStaffUser().getGroupId())
                .appRef(notificationDriverDto.appRef())
                .schoolAccount(studentStaffDetails.getAccountNumber())
                .schoolName(studentStaffDetails.getSchoolName())
                .studentStatus(StudentStatus.valueOf(notificationDriverDto.studentStatus()))
                .guardianPhoneNumber(studentStaffDetails.getGuardianPhoneNumber())
                .staffUsername(notificationDriverDto.performedByUsername())
                .studentUsername(notificationDriverDto.studentUsername())
                .build();

        var studentDay = new StudentDay();
        var studentTravel = new StudentTravel();

        if(StudentStatus.HOME_PICK_UP.equals(StudentStatus.valueOf(notificationDriverDto.studentStatus()))){
            Validate.isTrue(trip.getTripType().equals(TripType.PICK_UP), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS_FOR_TRIP, notificationDriverDto.studentUsername());

            var existingTravelDay = studentTravelRepository.findByStudentUsernameAndTripAndStudentStatus(notificationDriverDto.studentUsername(),
                    trip,
                    StudentStatus.valueOf(notificationDriverDto.studentStatus()));

            Validate.isTrue(existingTravelDay.isEmpty(),
                    ExceptionType.BAD_REQUEST,
                    STUDENT_ALREADY_PICKED_UP,
                    notificationDriverDto.studentUsername());

            checkIfStudentDayStatusExists(notificationDriverDto.studentUsername(), StudentStatus.valueOf(notificationDriverDto.studentStatus()), getCurrentOnlyDate());

            //Check if student has been recorded
            var existingStudentDay = studentDayRepository.findBySchoolDateAndStudentUsername(getCurrentOnlyDate(), notificationDriverDto.studentUsername());

            System.out.println("find record");
            System.out.println(existingStudentDay.isPresent());
            Validate.isTrue(existingStudentDay.isEmpty(),
                    ExceptionType.BAD_REQUEST,
                    STUDENT_ALREADY_HAS_EVENT,
                    notificationDriverDto.studentUsername());

            studentTravel.setStudentUsername(notificationDriverDto.studentUsername());
            studentTravel.setTrip(trip);
            studentTravel.setCreatedOn(getCurrentUTCTime());
            studentTravel.setModifiedOn(getCurrentUTCTime());
            studentTravel.setStudentStatus(StudentStatus.HOME_PICK_UP);
            studentTravel.setSchoolId(studentStaffDetails.getStudentSchoolId());

            studentTravelRepository.save(studentTravel);

            studentDay.setStudentStatus(StudentStatus.HOME_PICK_UP);
            studentDay.setOnTrip(Boolean.TRUE);
            studentDay.setCreatedOn(getCurrentUTCTime());
            studentDay.setModifiedOn(getCurrentOnlyDate());
            studentDay.setSchoolId(studentStaffDetails.getStudentSchoolId());
            studentDay.setStudentUsername(notificationDriverDto.studentUsername());
            studentDay.setStaffUsername(notificationDriverDto.performedByUsername());
            studentDay.setSchoolDate(getCurrentOnlyDate());

            studentDayRepository.save(studentDay);
            changeOpenTripToInProgress(trip);

            return sendSms(sendSmsDto);

        }

        var notificationDto = new NotificationDto(
                notificationDriverDto.studentUsername(),
                notificationDriverDto.studentStatus(),
                notificationDriverDto.performedByUsername(),
                notificationDriverDto.appRef()
        );

        if(StudentStatus.SCHOOL_SIGN_IN.equals(StudentStatus.valueOf(notificationDriverDto.studentStatus()))){
            Validate.isTrue(trip.getTripType().equals(TripType.PICK_UP), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS_FOR_TRIP, notificationDriverDto.studentUsername());

            //checkIfStudentDayStatusExists(notificationDriverDto.studentUsername(), StudentStatus.valueOf(notificationDriverDto.studentStatus()), getCurrentOnlyDate());

            var existingStudentTravel = studentTravelRepository.findByStudentUsernameAndTripAndStudentStatus(
                    notificationDto.studentUsername(),
                    trip,
                    StudentStatus.HOME_PICK_UP);

            Validate.isTrue(existingStudentTravel.isPresent(),
                    ExceptionType.RESOURCE_NOT_FOUND,
                    STUDENT_NOT_PICKED_UP,
                    notificationDto.studentUsername());

            var existingSignInStudentTravel = studentTravelRepository.findByStudentUsernameAndTripAndStudentStatus(
                    notificationDto.studentUsername(),
                    trip,
                    StudentStatus.SCHOOL_SIGN_IN);

            Validate.isTrue(existingSignInStudentTravel.isEmpty(),
                    ExceptionType.BAD_REQUEST,
                    STUDENT_IS_ALREADY_SIGNED_IN,
                    notificationDto.studentUsername());

            Validate.isTrue(existingStudentTravel.isPresent(),
                    ExceptionType.RESOURCE_NOT_FOUND,
                    STUDENT_NOT_PICKED_UP,
                    notificationDto.studentUsername());

            studentTravel.setStudentUsername(notificationDriverDto.studentUsername());
            studentTravel.setTrip(trip);
            studentTravel.setCreatedOn(getCurrentUTCTime());
            studentTravel.setModifiedOn(getCurrentUTCTime());
            studentTravel.setStudentStatus(StudentStatus.SCHOOL_SIGN_IN);
            studentTravel.setSchoolId(studentStaffDetails.getStudentSchoolId());

            studentTravelRepository.save(studentTravel);

            return sendNotificationStaff(notificationDto, Boolean.FALSE);
        }

        if(StudentStatus.SCHOOL_SIGN_OUT.equals(StudentStatus.valueOf(notificationDriverDto.studentStatus()))){
            Validate.isTrue(trip.getTripType().equals(TripType.DROP_OFF), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS_FOR_TRIP, notificationDriverDto.studentUsername());

            //checkIfStudentDayStatusExists(notificationDriverDto.studentUsername(), StudentStatus.valueOf(notificationDriverDto.studentStatus()), getCurrentOnlyDate());

            studentTravel.setStudentUsername(notificationDriverDto.studentUsername());
            studentTravel.setTrip(trip);
            studentTravel.setCreatedOn(getCurrentUTCTime());
            studentTravel.setModifiedOn(getCurrentUTCTime());
            studentTravel.setStudentStatus(StudentStatus.SCHOOL_SIGN_OUT);
            studentTravel.setSchoolId(studentStaffDetails.getStudentSchoolId());

            studentTravelRepository.save(studentTravel);
            changeOpenTripToInProgress(trip);

            return sendNotificationStaff(notificationDto, Boolean.TRUE);
        }

        if(StudentStatus.HOME_DROP_OFF.equals(StudentStatus.valueOf(notificationDriverDto.studentStatus()))){
            Validate.isTrue(trip.getTripType().equals(TripType.DROP_OFF), ExceptionType.BAD_REQUEST, INVALID_STUDENT_STATUS_FOR_TRIP, notificationDriverDto.studentUsername());

            checkIfStudentDayStatusExists(notificationDriverDto.studentUsername(), StudentStatus.valueOf(notificationDriverDto.studentStatus()), getCurrentOnlyDate());

            var existingDropOffStudentDay = studentDayRepository.findBySchoolDateAndStudentUsernameAndStudentStatus(getCurrentOnlyDate(),
                    notificationDriverDto.studentUsername(),
                    StudentStatus.valueOf(notificationDriverDto.studentStatus()));

            Validate.isTrue(existingDropOffStudentDay.isEmpty(),
                    ExceptionType.BAD_REQUEST,
                    STUDENT_ALREADY_DROPPED_OFF,
                    notificationDriverDto.studentUsername());

            var existingSignedOutStudentDay = studentDayRepository.findBySchoolDateAndStudentUsernameAndStudentStatus(getCurrentOnlyDate(),
                    notificationDriverDto.studentUsername(),
                    StudentStatus.SCHOOL_SIGN_OUT);

            Validate.isTrue(existingSignedOutStudentDay.isPresent(),
                    ExceptionType.RESOURCE_NOT_FOUND,
                    STUDENT_WAS_NOT_SIGNED_OUT,
                    notificationDriverDto.studentUsername());

            var existingStudentTravel = studentTravelRepository.findByStudentUsernameAndTripAndStudentStatus(notificationDriverDto.studentUsername(),
                    trip,
                    StudentStatus.SCHOOL_SIGN_OUT);

            Validate.isTrue(existingStudentTravel.isPresent(),
                    ExceptionType.BAD_REQUEST,
                    STUDENT_NOT_ON_TRIP,
                    notificationDriverDto.studentUsername());

            studentTravel.setStudentUsername(notificationDriverDto.studentUsername());
            studentTravel.setTrip(trip);
            studentTravel.setCreatedOn(getCurrentUTCTime());
            studentTravel.setModifiedOn(getCurrentUTCTime());
            studentTravel.setStudentStatus(StudentStatus.HOME_DROP_OFF);
            studentTravel.setSchoolId(studentStaffDetails.getStudentSchoolId());

            studentTravelRepository.save(studentTravel);

            studentDay.setStudentStatus(StudentStatus.HOME_DROP_OFF);
            studentDay.setOnTrip(Boolean.FALSE);
            studentDay.setCreatedOn(getCurrentUTCTime());
            studentDay.setModifiedOn(getCurrentOnlyDate());
            studentDay.setSchoolId(studentStaffDetails.getStudentSchoolId());
            studentDay.setStudentUsername(notificationDriverDto.studentUsername());
            studentDay.setStaffUsername(notificationDriverDto.performedByUsername());
            studentDay.setSchoolDate(getCurrentOnlyDate());

            studentDayRepository.save(studentDay);

            //changeOpenTripToInProgress(trip);

            return sendSms(sendSmsDto);
        }

        throw new BadRequestException("Failed to send notification");
    }

    @Transactional
    public Object bulkOnSchool(Long tripId){

        var existingTrip = tripRepository.findById(tripId);
        Validate.isPresent(existingTrip, TRIP_NOT_FOUND, tripId);

        var trip = existingTrip.get();
        Validate.isTrue(!trip.getTripStatus().equals(TripStatus.ENDED), ExceptionType.BAD_REQUEST, TRIP_ENDED);

        var studentTravelList = getStudentsOnPickUpTrip(trip);//studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.HOME_PICK_UP);

        for(StudentTravel obj : studentTravelList){
            var notificationDriverDto = new NotificationDriverDto(
                    tripId,
                    obj.getStudentUsername(),
                    StudentStatus.SCHOOL_SIGN_IN.name(),
                    trip.getStaffUsername(),
                    ("NC"+ UUID.randomUUID().toString().replace("-","")).substring(0, 10)
            );
            sendNotificationDriver(notificationDriverDto);
        }
        trip.setTripStatus(TripStatus.ENDED);
        trip.setModifiedOn(DateTimeUtil.getCurrentUTCTime());
        tripRepository.save(trip);

        return new BulkSignInResponse(
                trip,
                "SUCCESS",
                "Successfully signed into school"
        );
    }

    private List<StudentTravel> getStudentsOnPickUpTrip(Trip trip){

        var studentTravelPickUpList = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.HOME_PICK_UP);
        var studentTravelSignInList = studentTravelRepository.findAllByTripAndStudentStatus(trip, StudentStatus.SCHOOL_SIGN_IN);
        var pickUpList = new ArrayList<StudentTravel>();

        for(StudentTravel obj : studentTravelPickUpList){
            if(studentTravelSignInList.stream().noneMatch(t -> t.getStudentUsername().equals(obj.getStudentUsername()))){
                pickUpList.add(obj);
            }
        }

        return pickUpList;
    }

    public Object getDailyEventCount(String username){

        var eventUser = WebServiceUtil.getUserByUsername(username);

        var userGroup = getGroups().stream()
                .filter(groupVO -> groupVO.getId().equals(eventUser.getGroupId()))
                .findFirst();

        var groupName = userGroup.get().getName();
        var userRoles = getNotificationRolesByUserType(getUserTypeByGroupName(groupName));
        var listOfEventCounts = userStudentStatusCountRepository.findAllByUsernameAndDate(username, getCurrentOnlyDate());

        Validate.notNull(listOfEventCounts, ExceptionType.BAD_REQUEST, NO_EVENTS);

        var roleOne = listOfEventCounts.stream()
                .filter(event -> event.getStudentStatus().name().equals(userRoles.get(0).getRole().name()))
                .findFirst();
        var roleOneCount = 0L;

        if(roleOne.isPresent()){
            roleOneCount = roleOne.get().getDateCount();
        }

        var eventOne = new EventCountDto(getCurrentOnlyDate(), userRoles.get(0).getRole(), roleOneCount);

        var roleTwo = listOfEventCounts.stream()
                .filter(event -> event.getStudentStatus().name().equals(userRoles.get(1).getRole().name()))
                .findFirst();
        var roleTwoCount = 0L;

        if(roleTwo.isPresent()){
            roleTwoCount = roleTwo.get().getDateCount();
        }

        var eventTwo = new EventCountDto(getCurrentOnlyDate(), userRoles.get(1).getRole(), roleTwoCount);

        return Arrays.asList(
                eventOne, eventTwo
        );
    }

    public StudentDay getCurrentStudentDay(String studentUsername){
        var studentDay = studentDayRepository.findBySchoolDateAndStudentUsername(getCurrentOnlyDate(), studentUsername);
        Validate.isPresent(studentDay, STUDENT_NO_TODAY_RECORD, studentUsername);
        return studentDay.get();
    }

    public ImageUrlDto getProfileUrl(String username){

        var user = getUserByUsername(username);
        var imageUrls = user.getImages();

        return new ImageUrlDto(
                imageUrls.get(0).getThumbnailUrl().replace("127.0.0.1", hostIp),
                imageUrls.get(0).getFullUrl().replace("127.0.0.1", hostIp)
        );
    }

    public StudentResponseDto getMemberByUsername(String username){

        var result = getUserByUsername(username);

        var customFields = result.getFields();
        var guardianName = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("parent"))
                .findFirst();
        Validate.isTrue(guardianName.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "parent not found");

        var parentName = "";
        if(guardianName.isPresent())
            parentName = guardianName.get().getValue();

        var guardianPhoneNumber = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals(RECEIVER_PHONE))
                .findFirst();
        Validate.isTrue(guardianPhoneNumber.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "receiver_phone not found");

        var phoneNumber = "";
        if(guardianPhoneNumber.isPresent())
            phoneNumber = guardianPhoneNumber.get().getValue();

        var studentClass = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("std_class"))
                .findFirst();
        Validate.isTrue(studentClass.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "std_class not found");

        var stdClass = "";
        if(studentClass.isPresent())
            stdClass = studentClass.get().getValue();

        var school = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals(STUDENT_PHONE))
                .findFirst();
        Validate.isTrue(school.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "std_school not found");

        var studentSchool = "";
        if(school.isPresent())
            studentSchool = school.get().getValue();

        var schoolAccount = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("school_account"))
                .findFirst();
        Validate.isTrue(schoolAccount.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "school_account not found");

        Validate.isTrue(getAccountBalance(schoolAccount.get().getValue()).compareTo(BigDecimal.valueOf(200)) > 0, ExceptionType.BAD_REQUEST, SCHOOL_INSUFFICIENT_FUNDS);

        return  new StudentResponseDto(
                result.getId(),
                result.getName(),
                result.getUsername(),
                result.getEmail(),
                parentName,
                phoneNumber,
                stdClass,
                studentSchool
        );
    }

    public StaffDto checkCredentials(AuthenticationDto authenticationDto) throws Exception {

        authenticationDto.validate();

        var accessWebService = WebServiceUtil.getWebServiceFactory().getAccessWebService();

        var params = new CheckCredentialsParameters();
        params.setPrincipal(authenticationDto.username());
        params.setCredentials(authenticationDto.pin());
        params.setPrincipalType(PRINCIPLE_TYPE);

        var result = accessWebService.checkCredentials(params);

        Validate.isTrue(!CredentialsStatus.BLOCKED.equals(result), ExceptionType.BAD_CREDENTIALS, USER_BLOCKED, authenticationDto.username());
        Validate.isTrue(!CredentialsStatus.INVALID.equals(result), ExceptionType.BAD_CREDENTIALS, INVALID_CREDENTIALS);

        var user = WebServiceUtil.getUserByUsername(authenticationDto.username());
        log.info(user.toString());
        var customFields = user.getFields();

        var school = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals(STUDENT_PHONE))
                .findFirst();
        Validate.isTrue(school.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "school account not found");

        var staffSchool = "";
        if(school.isPresent())
            staffSchool = school.get().getValue();

        var phoneNumber = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals(RECEIVER_PHONE))
                .findFirst();
        Validate.isTrue(phoneNumber.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "receiver_phone account not found");

        var staffContact = "";
        if(phoneNumber.isPresent())
            staffContact = phoneNumber.get().getValue();

        var userGroup = getGroups().stream()
                .filter(groupVO -> groupVO.getId().equals(user.getGroupId()))
                .findFirst();

        var groupName = "";
        if(userGroup.isPresent())
            groupName = userGroup.get().getName();

        var userTpe = getUserTypeByGroupName(groupName);
        List<NotificationRole> roles = getNotificationRolesByUserType(userTpe);

        return new StaffDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                staffContact,
                staffSchool,
                userTpe,
                roles
        );
    }

    private void updateUserStudentStatusCount(String username, UserType userType, Date date, StudentStatus status) {

        var userStudentStatusCount = userStudentStatusCountRepository.findByUsernameAndDateAndStudentStatus(username, date, status);

        if(userStudentStatusCount.isPresent()){
            var count = userStudentStatusCount.get();
            count.setDateCount(count.getDateCount() + 1L);
            count.setModifiedOn(DateTimeUtil.getCurrentUTCTime());

            userStudentStatusCountRepository.save(count);
        }else{
            var count = new UserStudentStatusCount();
            count.setUsername(username);
            count.setStudentStatus(status);
            count.setDateCount(1L);
            count.setUserType(userType);
            count.setDate(getCurrentOnlyDate());
            count.setCreatedOn(DateTimeUtil.getCurrentUTCTime());

            log.info(count.toString());

            userStudentStatusCountRepository.save(count);

            log.info("persisting event object");
        }
    }

    private StudentStaffDetailsDto getStudentAndStaffDetails(String studentUsername, String staffUsername){

        var staffUser = WebServiceUtil.getUserByUsername(staffUsername);
        var studentUser = WebServiceUtil.getUserByUsername(studentUsername);

        var customFieldsStaff = staffUser.getFields();
        var customFieldsStudent = studentUser.getFields();

        var staffSchoolId = customFieldsStaff.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("school_id"))
                .findFirst();
        Validate.isTrue(staffSchoolId.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "school_id not found");

        String schoolIdStaff = "";
        if(staffSchoolId.isPresent())
            schoolIdStaff = staffSchoolId.get().getValue();

        var studentSchoolId = customFieldsStudent.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("school_id"))
                .findFirst();
        Validate.isTrue(studentSchoolId.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "school_id not found");

        String schoolIdStudent = "";
        if(studentSchoolId.isPresent())
            schoolIdStudent = studentSchoolId.get().getValue();

        var guardianPhoneNumber = customFieldsStudent.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals(RECEIVER_PHONE))
                .findFirst();
        Validate.isTrue(guardianPhoneNumber.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "receiver_phone not found");

        String phoneNumber = "";
        if(guardianPhoneNumber.isPresent())
            phoneNumber = guardianPhoneNumber.get().getValue();

        var schoolAccount = customFieldsStudent.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("school_account"))
                .findFirst();
        Validate.isTrue(schoolAccount.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "school account not found");

        String accountNumber = "";
        if(schoolAccount.isPresent())
            accountNumber = schoolAccount.get().getValue();

        var studentSchool = customFieldsStudent.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals(STUDENT_PHONE))
                .findFirst();
        Validate.isTrue(studentSchool.isPresent(), ExceptionType.RESOURCE_NOT_FOUND, "school name not found");

        String schoolName = "";
        if(studentSchool.isPresent())
            schoolName = studentSchool.get().getValue();

        return StudentStaffDetailsDto.builder()
                .schoolName(schoolName)
                .studentSchoolId(schoolIdStudent)
                .studentUser(studentUser)
                .staffUser(staffUser)
                .staffSchoolId(schoolIdStaff)
                .accountNumber(accountNumber)
                .guardianPhoneNumber(phoneNumber)
                .build();
    }

    private NotificationResponseDto sendSms(SendSmsDto sendSmsDto){

        PaymentDto paymentDto = PaymentDto.builder()
                .appRef(sendSmsDto.getAppRef())
                .mainSmsAccount(mainSmsAccount)
                .schoolAccount(sendSmsDto.getSchoolAccount())
                .smsTransferType(smsPaymentTransferType)
                .studentStatus(sendSmsDto.getStudentStatus())
                .staffUsername(sendSmsDto.getStaffUsername())
                .studentSchoolName(sendSmsDto.getSchoolName())
                .build();

        var paymentResult = WebServiceUtil.makeCyclosPayment(paymentDto);

        var userGroup = getGroups().stream()
                .filter(groupVO -> groupVO.getId().equals(sendSmsDto.getStaffUserGroupId()))
                .findFirst();

        var groupName = "";
        if(userGroup.isPresent())
            groupName = userGroup.get().getName();

        var userType = getUserTypeByGroupName(groupName);
        var performerType = userType.name().toLowerCase().replace("_", " ");

        var message = getSms(StudentStatus.valueOf(sendSmsDto.getStudentStatus().name()),
                sendSmsDto.getStudentUsername(),
                trimSchoolName(sendSmsDto.getSchoolName()),
                performerType,
                sendSmsDto.getStaffUsername());

        var notification = new Notification();
        notification.setTransactionId(paymentResult.getTransfer().getTransactionNumber());
        notification.setMessage(message);
        notification.setSender("SAFE CHILD");
        notification.setReceiver(sendSmsDto.getGuardianPhoneNumber());
        notification.setStatus(SmsStatus.PENDING);
        //notification.setTransactionId(UUID.randomUUID().toString().replace("-",""));
        notification.setCreatedOn(DateTimeUtil.getCurrentUTCTime());

        System.out.println(notification.toString());

        var notify = notificationRepository.save(notification);
        messageBrokerService.sendSms(notify);

        updateUserStudentStatusCount(sendSmsDto.getStaffUsername(),
                userType,
                getCurrentOnlyDate(),
                StudentStatus.valueOf(sendSmsDto.getStudentStatus().name()));

        return new NotificationResponseDto(
                "Success",
                sendSmsDto.getStudentStatus(),
                sendSmsDto.getAppRef(),
                null
        );
    }

    private String getSms(StudentStatus status,
                          String childId,
                          String schoolName,
                          String performer,
                          String performerId){

        var time = LocalTime.now();
        var formatter = DateTimeFormatter.ofPattern("HH:mm");

        if(status.equals(StudentStatus.HOME_PICK_UP)){
            return String.format(pickUpSms, childId, performer, performerId, time.format(formatter));
        }

        if(status.equals(StudentStatus.HOME_DROP_OFF)){
            return String.format(dropOffSms, childId, schoolName, performer, performerId, time.format(formatter));
        }

        if(status.equals(StudentStatus.SCHOOL_SIGN_OUT)){
            return String.format(offSchoolSms, childId, performer, performerId, schoolName, time.format(formatter));
        }

        if(status.equals(StudentStatus.SCHOOL_SIGN_IN)){
            return String.format(onSchoolSms, childId, schoolName, performer, performerId, time.format(formatter));
        }

        return null;
    }

    private void changeOpenTripToInProgress(Trip trip){
        if(trip.getTripStatus().equals(TripStatus.OPEN)){
            trip.setTripStatus(TripStatus.IN_PROGRESS);
            tripRepository.save(trip);
        }
    }

    private void checkIfStudentDayStatusExists(String username, StudentStatus studentStatus, Date schoolDate){
         var existingStudentDay = studentDayRepository.findBySchoolDateAndStudentUsernameAndStudentStatus(schoolDate,
                 username,
                 studentStatus);

        System.out.println("check if empty");
        System.out.println(existingStudentDay.isEmpty());

        System.out.println("check if present");
        System.out.println(existingStudentDay.isPresent());

         Validate.isTrue(existingStudentDay.isEmpty(), ExceptionType.BAD_REQUEST, "%s has already been %s");
    }

}
