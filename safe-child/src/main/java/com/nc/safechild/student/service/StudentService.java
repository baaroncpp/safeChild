package com.nc.safechild.student.service;

import com.nc.safechild.exceptions.BadRequestException;
import com.nc.safechild.exceptions.model.ExceptionType;
import com.nc.safechild.network.MessageBrokerService;
import com.nc.safechild.student.model.NotificationRole;
import com.nc.safechild.student.model.dto.*;
import com.nc.safechild.student.model.enums.NotificationRoleEnum;
import com.nc.safechild.student.model.enums.SmsStatus;
import com.nc.safechild.student.model.enums.StudentStatus;
import com.nc.safechild.student.model.enums.UserType;
import com.nc.safechild.student.model.jpa.Notification;
import com.nc.safechild.student.model.jpa.UserStudentStatusCount;
import com.nc.safechild.student.respository.NotificationRepository;
import com.nc.safechild.student.respository.UserStudentStatusCountRepository;
import com.nc.safechild.utils.DateTimeUtil;
import com.nc.safechild.utils.Validate;
import com.nc.safechild.utils.WebServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.strohalm.cyclos.webservices.access.CheckCredentialsParameters;
import nl.strohalm.cyclos.webservices.access.CredentialsStatus;
import nl.strohalm.cyclos.webservices.accounts.AccountHistorySearchParameters;
import nl.strohalm.cyclos.webservices.model.FieldValueVO;
import nl.strohalm.cyclos.webservices.model.GroupVO;
import nl.strohalm.cyclos.webservices.model.MemberVO;
import nl.strohalm.cyclos.webservices.payments.PaymentParameters;
import nl.strohalm.cyclos.webservices.payments.PaymentResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.nc.safechild.utils.MessageConstants.*;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/20/23
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private static final String PRINCIPLE_TYPE = "USER";

    @Value("${accounts.sms}")
    private String mainSmsAccount;

    @Value("${transfer-type.sms-notification}")
    private Long smsPaymentTransferType;

    @Value("${notification.sms.pick-up}")
    private String pickUpSms;

    @Value("${notification.sms.drop-off}")
    private String dropOffSms;

    @Value("${notification.sms.on-school}")
    private String onSchoolSms;

    @Value("${notification.sms.off-school}")
    private String offSchoolSms;

    private final NotificationRepository notificationRepository;
    private final MessageBrokerService messageBrokerService;
    private final UserStudentStatusCountRepository userStudentStatusCountRepository;

    public Object getSchoolBurg(){
        var accessWebService = WebServiceUtil.getWebServiceFactory().getMemberWebService();
        return null;
    }

    public Object getMemberByUsername(String username){

        var result = getUserByUsername(username);

        var customFields = result.getFields();
        var guardianName = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("parent"))
                .findFirst().get()
                .getValue();

        var guardianPhoneNumber = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("reciever_phone"))
                .findFirst().get()
                .getValue();

        var studentClass = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("std_class"))
                .findFirst().get()
                .getValue();

        var school = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("std_school"))
                .findFirst().get()
                .getValue();

        var schoolAccount = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("school_account"))
                .findFirst().get()
                .getValue();

        Validate.isTrue(getAccountBalance(schoolAccount).compareTo(BigDecimal.valueOf(200)) > 0, ExceptionType.BAD_REQUEST, SCHOOL_INSUFFICIENT_FUNDS);

        return  new StudentResponseDto(
                result.getId(),
                result.getName(),
                result.getUsername(),
                result.getEmail(),
                guardianName,
                guardianPhoneNumber,
                studentClass,
                school
        );
    }

    public Object checkCredentials(AuthenticationDto authenticationDto) throws Exception {

        authenticationDto.validate();

        var accessWebService = WebServiceUtil.getWebServiceFactory().getAccessWebService();

        CheckCredentialsParameters params = new CheckCredentialsParameters();
        params.setPrincipal(authenticationDto.username());
        params.setCredentials(authenticationDto.pin());
        params.setPrincipalType(PRINCIPLE_TYPE);

        var result = accessWebService.checkCredentials(params);

        Validate.isTrue(!CredentialsStatus.BLOCKED.equals(result), ExceptionType.BAD_CREDENTIALS, USER_BLOCKED, authenticationDto.username());
        Validate.isTrue(!CredentialsStatus.INVALID.equals(result), ExceptionType.BAD_CREDENTIALS, INVALID_CREDENTIALS);

        var user = getUserByUsername(authenticationDto.username());
        log.info(user.toString());
        var customFields = user.getFields();

        var school = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("std_school"))
                .findFirst().get()
                .getValue();

        var phoneNumber = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("reciever_phone"))
                .findFirst().get()
                .getValue();

        var userGroup = getGroups().stream()
                .filter(groupVO -> groupVO.getId().equals(user.getGroupId()))
                .findFirst();

        var groupName = userGroup.get().getName();

        var userTpe = getUserTypeByGroupName(groupName);
        List<NotificationRole> roles = getNotificationRolesByUserType(userTpe);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(),
                phoneNumber,
                school,
                userTpe,
                roles
        );
    }

    public BigDecimal getAccountBalance(String accountNumber){

        var accountWebService = WebServiceUtil.getWebServiceFactory().getAccountWebService();

        var params = new AccountHistorySearchParameters();
        params.setPrincipal(accountNumber);
        params.setPrincipalType("USER");

        var accountHistory = accountWebService.searchAccountHistory(params);

        log.info(accountHistory.getAccountStatus().toString());

        return accountHistory.getAccountStatus().getBalance();
    }

    private MemberVO getUserByUsername(String username){
        var memberWebService = WebServiceUtil.getWebServiceFactory().getMemberWebService();

        var result = memberWebService.loadByUsername(username);
        Validate.notNull(result, ExceptionType.RESOURCE_NOT_FOUND, STUDENT_NOT_FOUND, username);

        return result;
    }

    public List<GroupVO> getGroups(){
        var memberWebService = WebServiceUtil.getWebServiceFactory().getMemberWebService();
        return memberWebService.listManagedGroups();
    }

    private UserType getUserTypeByGroupName(String groupName){

        if(groupName.contains("TEACHER")){
            return UserType.TEACHER;
        }

        if(groupName.contains("DRIVER")){
            return UserType.DRIVER;
        }

        if(groupName.contains("GATE")){
            return UserType.GATE_MAN;
        }

        return UserType.STUDENT;
    }

    private List<NotificationRole> getNotificationRolesByUserType(UserType userType){

        if(userType.equals(UserType.DRIVER)){
            return Arrays.asList(
                    new NotificationRole(NotificationRoleEnum.PICK_UP, NotificationRoleEnum.PICK_UP.getDescription()),
                    new NotificationRole(NotificationRoleEnum.DROP_OFF, NotificationRoleEnum.DROP_OFF.getDescription())
            );
        }

        if(userType.equals(UserType.GATE_MAN)){
            return Arrays.asList(
                    new NotificationRole(NotificationRoleEnum.ON_SCHOOL, NotificationRoleEnum.ON_SCHOOL.getDescription()),
                    new NotificationRole(NotificationRoleEnum.OFF_SCHOOL, NotificationRoleEnum.OFF_SCHOOL.getDescription())
            );
        }

        if(userType.equals(UserType.TEACHER)){
            return Arrays.asList(
                    new NotificationRole(NotificationRoleEnum.ON_SCHOOL, NotificationRoleEnum.ON_SCHOOL.getDescription()),
                    new NotificationRole(NotificationRoleEnum.OFF_SCHOOL, NotificationRoleEnum.OFF_SCHOOL.getDescription())
            );
        }

        return Arrays.asList(
                new NotificationRole(NotificationRoleEnum.PICK_UP, NotificationRoleEnum.PICK_UP.getDescription()),
                new NotificationRole(NotificationRoleEnum.DROP_OFF, NotificationRoleEnum.DROP_OFF.getDescription())
        );
    }

    public Object sendNotification(NotificationDto notificationDto){

        notificationDto.validate();

        var user = getUserByUsername(notificationDto.studentUsername());
        var eventUser = getUserByUsername(notificationDto.performedByUsername());
        var customFields = user.getFields();

        var schoolAccount = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("school_account"))
                .findFirst().get()
                .getValue();

        var studentSchool = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("std_school"))
                .findFirst().get()
                .getValue();

        var phoneNumber = customFields.stream()
                .filter(fieldValueVO -> fieldValueVO.getInternalName().equals("reciever_phone"))
                .findFirst().get()
                .getValue();

        var paymentWebService = WebServiceUtil.getWebServiceFactory().getPaymentWebService();

        List<FieldValueVO> customParams = Arrays.asList(
                new FieldValueVO(STUDENT_SCHOOL, studentSchool),
                new FieldValueVO(PERFORMED_BY, eventUser.getUsername()),
                new FieldValueVO(APP_REF, notificationDto.appRef()),
                new FieldValueVO(STUDENT_STATUS, notificationDto.studentStatus())
        );

        var params = new PaymentParameters();
        params.setFromMemberPrincipalType(MEMBER_PRINCIPLE_TYPE);
        params.setToMemberPrincipalType(MEMBER_PRINCIPLE_TYPE);
        params.setFromMember(schoolAccount);
        params.setToMember(mainSmsAccount);
        params.setTransferTypeId(smsPaymentTransferType);
        params.setCustomValues(customParams);
        params.setAmount(BigDecimal.valueOf(150));
        params.setDescription("SMS CHARGE");

        log.info(params.toString());

        var userGroup = getGroups().stream()
                .filter(groupVO -> groupVO.getId().equals(eventUser.getGroupId()))
                .findFirst();

        var groupName = userGroup.get().getName();
        var userType = getUserTypeByGroupName(groupName);
        var performerType = userType.name().toLowerCase().replace("_", " ");

        var message = getSms(StudentStatus.valueOf(notificationDto.studentStatus()),
                notificationDto.studentUsername(),
                schoolAccount,
                performerType,
                notificationDto.performedByUsername());

        var result =  paymentWebService.doPayment(params);
        processPaymentStatus(result);

        var notification = new Notification();
        notification.setTransactionId(result.getTransfer().getTransactionNumber());
        notification.setMessage(message);
        notification.setSender("SAFE CHILD");
        notification.setReceiver(phoneNumber);
        notification.setStatus(SmsStatus.PENDING);
        notification.setTransactionId(UUID.randomUUID().toString().replace("-",""));
        notification.setCreatedOn(DateTimeUtil.getCurrentUTCTime());

        var notify = notificationRepository.save(notification);
        messageBrokerService.sendSms(notify);

        updateUserStudentStatusCount(eventUser.getUsername(),
                userType,
                getCurrentDate(),
                StudentStatus.valueOf(notificationDto.studentStatus()));

        return new NotificationResponseDto(
                result.getStatus().name(),
                StudentStatus.valueOf(notificationDto.studentStatus()),
                notificationDto.appRef()
        );
    }

    public Object getDailyEventCount(String username){

        var eventUser = getUserByUsername(username);

        var userGroup = getGroups().stream()
                .filter(groupVO -> groupVO.getId().equals(eventUser.getGroupId()))
                .findFirst();

        var groupName = userGroup.get().getName();
        var userRoles = getNotificationRolesByUserType(getUserTypeByGroupName(groupName));
        var listOfEventCounts = userStudentStatusCountRepository.findAllByUsernameAndDate(username, getCurrentDate());

        Validate.notNull(listOfEventCounts, ExceptionType.BAD_REQUEST, NO_EVENTS);

        var roleOne = listOfEventCounts.stream()
                .filter(event -> event.getStudentStatus().name().equals(userRoles.get(0).getRole().name()))
                .findFirst();
        var roleOneCount = 0L;

        if(roleOne.isPresent()){
            roleOneCount = roleOne.get().getDateCount();
        }

        var eventOne = new EventCountDto(getCurrentDate(), NotificationRoleEnum.valueOf(roleOne.get().getStudentStatus().name()), roleOneCount);

        var roleTwo = listOfEventCounts.stream()
                .filter(event -> event.getStudentStatus().name().equals(userRoles.get(1).getRole().name()))
                .findFirst();
        var roleTwoCount = 0L;

        if(roleTwo.isPresent()){
            roleTwoCount = roleTwo.get().getDateCount();
        }

        var eventTwo = new EventCountDto(getCurrentDate(), NotificationRoleEnum.valueOf(roleTwo.get().getStudentStatus().name()), roleTwoCount);

        return Arrays.asList(
                eventOne, eventTwo
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
            count.setDate(getCurrentDate());
            count.setCreatedOn(DateTimeUtil.getCurrentUTCTime());

            log.info(count.toString());

            userStudentStatusCountRepository.save(count);

            log.info("persisting event object");
        }
    }

    private Date getCurrentDate(){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date today = DateTimeUtil.getCurrentUTCTime();
        try {
            return formatter.parse(formatter.format(today));
        } catch (ParseException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private void processPaymentStatus(PaymentResult result){
        String responseMsg;
        var throwException = Boolean.TRUE;
        switch (result.getStatus()) {
            case PROCESSED:
                throwException = Boolean.FALSE;
                String transactionNumber = result.getTransfer().getTransactionNumber();
                responseMsg = "The payment was successful. The transaction number is " + transactionNumber;
                log.info(responseMsg);
                break;
            case PENDING_AUTHORIZATION:
                responseMsg = "The payment is awaiting authorization";
                log.error(responseMsg);
                break;
            case INVALID_CHANNEL:
                responseMsg = "The given user cannot access this channel";
                log.error(responseMsg);
                break;
            case INVALID_CREDENTIALS:
                responseMsg = "You have entered an invalid PIN";
                log.error(responseMsg);
                break;
            case BLOCKED_CREDENTIALS:
                responseMsg = "Your PIN is blocked by exceeding trials";
                log.error(responseMsg);
                break;
            case INVALID_PARAMETERS:
                responseMsg = "Please, check the given parameters";
                log.error(responseMsg);
                break;
            case NOT_ENOUGH_CREDITS:
                responseMsg = "You don't have enough funds for this payment";
                log.error(responseMsg);
                break;
            case MAX_DAILY_AMOUNT_EXCEEDED:
                responseMsg = "You have already exceeded the maximum amount today";
                log.error(responseMsg);
                break;
            default:
                responseMsg = "There was an error on the payment: " + result.getStatus();
                log.error(responseMsg);
        }

        if(Boolean.TRUE.equals(throwException)){
            throw new BadRequestException(responseMsg);
        }
    }

    private String getSms(StudentStatus status,
                         String childId,
                         String schoolId,
                         String performer,
                         String performerId){

        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        if(status.equals(StudentStatus.PICK_UP)){
            return String.format(pickUpSms, childId, performer, performerId, time.format(formatter));
        }

        if(status.equals(StudentStatus.DROP_OFF)){
            return String.format(dropOffSms, childId, schoolId, performer, performerId, time.format(formatter));
        }

        if(status.equals(StudentStatus.OFF_SCHOOL)){
            return String.format(offSchoolSms, childId, performer, performerId, schoolId, time.format(formatter));
        }

        if(status.equals(StudentStatus.ON_SCHOOL)){
            return String.format(onSchoolSms, childId, schoolId, performer, performerId, time.format(formatter));
        }

        return null;
    }

}
