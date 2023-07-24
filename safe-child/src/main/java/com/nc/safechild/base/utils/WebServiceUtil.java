package com.nc.safechild.base.utils;

import com.nc.safechild.exceptions.BadRequestException;
import com.nc.safechild.exceptions.model.ExceptionType;
import com.nc.safechild.student.models.dto.PaymentDto;
import com.nc.safechild.student.models.enums.NotificationRoleEnum;
import com.nc.safechild.student.models.enums.StudentStatus;
import com.nc.safechild.student.models.NotificationRole;
import com.nc.safechild.student.models.enums.UserType;
import com.nc.safechild.trip.model.enums.TripType;
import lombok.extern.slf4j.Slf4j;
import nl.strohalm.cyclos.webservices.CyclosWebServicesClientFactory;
import nl.strohalm.cyclos.webservices.accounts.AccountHistorySearchParameters;
import nl.strohalm.cyclos.webservices.model.FieldValueVO;
import nl.strohalm.cyclos.webservices.model.GroupVO;
import nl.strohalm.cyclos.webservices.model.MemberVO;
import nl.strohalm.cyclos.webservices.payments.PaymentParameters;
import nl.strohalm.cyclos.webservices.payments.PaymentResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.nc.safechild.base.utils.MessageConstants.*;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/20/23
 **/
@Slf4j
public class WebServiceUtil {

    private WebServiceUtil() { }

    private static final String CORE_BANKING_URL = "http://127.0.0.1:8080/platform";

    public static CyclosWebServicesClientFactory getWebServiceFactory(){
        var factory = new CyclosWebServicesClientFactory();
        factory.setServerRootUrl(CORE_BANKING_URL);
        factory.setUsername("web");
        factory.setPassword("web@1234");
        return factory;
    }

    public static MemberVO getUserByUsername(String username){
        var memberWebService = WebServiceUtil.getWebServiceFactory().getMemberWebService();

        MemberVO result = null;
        try {
            result = memberWebService.loadByUsername(username);
        }catch(Exception e){
            var errorMsg = e.getMessage();
            Validate.filterException(errorMsg.substring(errorMsg.lastIndexOf(":") + 1));
        }
        Validate.notNull(result, ExceptionType.RESOURCE_NOT_FOUND, MessageConstants.STUDENT_NOT_FOUND, username);

        return result;
    }

    public static BigDecimal getAccountBalance(String accountNumber){

        var accountWebService = WebServiceUtil.getWebServiceFactory().getAccountWebService();

        var params = new AccountHistorySearchParameters();
        params.setPrincipal(accountNumber);
        params.setPrincipalType("USER");

        var accountHistory = accountWebService.searchAccountHistory(params);

        log.info(accountHistory.getAccountStatus().toString());

        return accountHistory.getAccountStatus().getBalance();
    }

    public static PaymentResult makeCyclosPayment(PaymentDto paymentDto){
        var paymentWebService = WebServiceUtil.getWebServiceFactory().getPaymentWebService();

        List<FieldValueVO> customParams = Arrays.asList(
                new FieldValueVO(STUDENT_SCHOOL, paymentDto.getStudentSchoolName()),
                new FieldValueVO(PERFORMED_BY, paymentDto.getStaffUsername()),
                new FieldValueVO(APP_REF, paymentDto.getAppRef()),
                new FieldValueVO(STUDENT_STATUS, paymentDto.getStudentStatus().name())
        );

        var params = new PaymentParameters();
        params.setFromMemberPrincipalType(MEMBER_PRINCIPLE_TYPE);
        params.setToMemberPrincipalType(MEMBER_PRINCIPLE_TYPE);
        params.setFromMember(paymentDto.getSchoolAccount());
        params.setToMember(paymentDto.getMainSmsAccount());
        params.setTransferTypeId(paymentDto.getSmsTransferType());
        params.setCustomValues(customParams);
        params.setAmount(BigDecimal.valueOf(150));
        params.setDescription("SMS CHARGE");

        log.info(params.toString());

        var result =  paymentWebService.doPayment(params);
        processPaymentStatus(result);

        return result;
    }

    public static String trimSchoolName(String schoolName){
        if(schoolName.length() > 60){
            return schoolName.substring(0, 58) + "...";
        }else{
            return schoolName;
        }
    }

    public static boolean isStudentStatus(String value){
        List<String> studentStatusList = Arrays.asList(
                StudentStatus.SCHOOL_SIGN_OUT.name(),
                StudentStatus.HOME_PICK_UP.name(),
                StudentStatus.SCHOOL_SIGN_IN.name(),
                StudentStatus.HOME_DROP_OFF.name(),
                StudentStatus.IN_CLASS.name()
        );
        return studentStatusList.contains(value);
    }

    public static List<GroupVO> getGroups(){
        var memberWebService = WebServiceUtil.getWebServiceFactory().getMemberWebService();
        return memberWebService.listManagedGroups();
    }

    public static UserType getUserTypeByGroupName(String groupName){

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

    public static List<NotificationRole> getNotificationRolesByUserType(UserType userType){

        if(userType.equals(UserType.DRIVER)){
            return Arrays.asList(
                    new NotificationRole(NotificationRoleEnum.PICK_UP, NotificationRoleEnum.PICK_UP.getDescription()),
                    new NotificationRole(NotificationRoleEnum.DROP_OFF, NotificationRoleEnum.DROP_OFF.getDescription()),
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

    public static boolean isTripType(String value){
        List<String> tripTypeList = Arrays.asList(
                TripType.PICK_UP.name(),
                TripType.DROP_OFF.name()
        );

        return tripTypeList.contains(value);
    }

    public static void processPaymentStatus(PaymentResult result){
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

}
