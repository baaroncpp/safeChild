package com.bwongo.core.core_banking.utils;

import com.bwongo.commons.models.exceptions.BadRequestException;
import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.enums.NotificationRoleEnum;
import com.bwongo.core.base.model.enums.StudentStatus;
import com.bwongo.core.base.model.enums.TripType;
import com.bwongo.core.base.model.enums.UserTypeEnum;
import lombok.extern.slf4j.Slf4j;
import nl.strohalm.cyclos.webservices.CyclosWebServicesClientFactory;
import nl.strohalm.cyclos.webservices.accounts.AccountHistorySearchParameters;
import nl.strohalm.cyclos.webservices.model.FieldValueVO;
import nl.strohalm.cyclos.webservices.model.GroupVO;
import nl.strohalm.cyclos.webservices.model.MemberVO;
import nl.strohalm.cyclos.webservices.payments.PaymentParameters;
import nl.strohalm.cyclos.webservices.payments.PaymentResult;
import static com.bwongo.core.core_banking.utils.CoreBankingMsgConstant.*;
import com.bwongo.core.core_banking.model.dto.PaymentDto;
import com.bwongo.core.notify_mgt.model.dto.*;
import static com.bwongo.core.student_mgt.utils.StudentMsgConstant.STUDENT_NOT_FOUND;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 10/2/23
 **/
@Slf4j
public class CoreBankingWebServiceUtils {

    private CoreBankingWebServiceUtils() { }

    private static final String CORE_BANKING_URL = "http://127.0.0.1:8080/platform";

    public static CyclosWebServicesClientFactory getWebServiceFactory(){
        var factory = new CyclosWebServicesClientFactory();
        factory.setServerRootUrl(CORE_BANKING_URL);
        factory.setUsername("web");
        factory.setPassword("web@1234");
        return factory;
    }

    public static BigDecimal getAccountBalance(String accountNumber){

        var accountWebService = CoreBankingWebServiceUtils.getWebServiceFactory().getAccountWebService();

        var params = new AccountHistorySearchParameters();
        params.setPrincipal(accountNumber);
        params.setPrincipalType("USER");

        var accountHistory = accountWebService.searchAccountHistory(params);

        log.info(accountHistory.getAccountStatus().toString());

        return accountHistory.getAccountStatus().getBalance();
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

    public static MemberVO getUserByUsername(String username){
        var memberWebService = CoreBankingWebServiceUtils.getWebServiceFactory().getMemberWebService();

        MemberVO result = null;
        try {
            result = memberWebService.loadByUsername(username);
        }catch(Exception e){
            var errorMsg = e.getMessage();
            Validate.filterException(errorMsg.substring(errorMsg.lastIndexOf(":") + 1));
        }
        Validate.notNull(result, ExceptionType.RESOURCE_NOT_FOUND, STUDENT_NOT_FOUND, username);

        return result;
    }

    public static PaymentResult makeCyclosPayment(PaymentDto paymentDto){
        var paymentWebService = CoreBankingWebServiceUtils.getWebServiceFactory().getPaymentWebService();

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
        var memberWebService = CoreBankingWebServiceUtils.getWebServiceFactory().getMemberWebService();
        return memberWebService.listManagedGroups();
    }

    public static UserTypeEnum getUserTypeByGroupName(String groupName){

        if(groupName.contains("STAFF")){
            return UserTypeEnum.SCHOOL_STAFF;
        }

        if(groupName.contains("DRIVER")){
            return UserTypeEnum.DRIVER;
        }

        if(groupName.contains("SCHOOL_ADMIN")){
            return UserTypeEnum.SCHOOL_ADMIN;
        }

        return UserTypeEnum.SCHOOL_ADMIN;
    }

    public static List<NotificationRoleDto> getNotificationRolesByUserType(UserTypeEnum userType){

        if(userType.equals(UserTypeEnum.DRIVER)){
            return Arrays.asList(
                    new NotificationRoleDto(NotificationRoleEnum.HOME_PICK_UP, NotificationRoleEnum.HOME_PICK_UP.getDescription()),
                    new NotificationRoleDto(NotificationRoleEnum.HOME_DROP_OFF, NotificationRoleEnum.HOME_DROP_OFF.getDescription()),
                    new NotificationRoleDto(NotificationRoleEnum.SCHOOL_SIGN_IN, NotificationRoleEnum.SCHOOL_SIGN_IN.getDescription()),
                    new NotificationRoleDto(NotificationRoleEnum.SCHOOL_SIGN_OUT, NotificationRoleEnum.SCHOOL_SIGN_OUT.getDescription())
            );
        }

        if(userType.equals(UserTypeEnum.SCHOOL_STAFF)){
            return Arrays.asList(
                    new NotificationRoleDto(NotificationRoleEnum.SCHOOL_SIGN_IN, NotificationRoleEnum.SCHOOL_SIGN_IN.getDescription()),
                    new NotificationRoleDto(NotificationRoleEnum.SCHOOL_SIGN_OUT, NotificationRoleEnum.SCHOOL_SIGN_OUT.getDescription())
            );
        }

        return Arrays.asList(
                new NotificationRoleDto(NotificationRoleEnum.SCHOOL_SIGN_IN, NotificationRoleEnum.SCHOOL_SIGN_IN.getDescription()),
                new NotificationRoleDto(NotificationRoleEnum.SCHOOL_SIGN_OUT, NotificationRoleEnum.SCHOOL_SIGN_OUT.getDescription())
        );
    }

    public static boolean isTripType(String value){
        List<String> tripTypeList = Arrays.asList(
                TripType.PICK_UP.name(),
                TripType.DROP_OFF.name()
        );

        return tripTypeList.contains(value);
    }

    public static boolean isValidDate(String date){
       SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        // With lenient parsing, the parser may use heuristics to interpret
        // inputs that do not precisely match this object's format.
        format.setLenient(false);
        try {
            format.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

}
