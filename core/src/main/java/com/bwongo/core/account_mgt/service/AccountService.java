package com.bwongo.core.account_mgt.service;

import com.bwongo.commons.models.exceptions.BadRequestException;
import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.text.StringUtil;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.account_mgt.model.dto.*;
import com.bwongo.core.account_mgt.model.enums.*;
import com.bwongo.core.account_mgt.model.jpa.TAccount;
import com.bwongo.core.account_mgt.model.jpa.TAccountTransaction;
import com.bwongo.core.account_mgt.model.jpa.TCashFlow;
import com.bwongo.core.account_mgt.model.jpa.TMomoDeposit;
import com.bwongo.core.account_mgt.network.ZengaPayApiCall;
import com.bwongo.core.account_mgt.repository.*;
import com.bwongo.core.base.service.AuditService;
import com.bwongo.core.core_banking.model.dto.MomoBankingDto;
import com.bwongo.core.core_banking.model.dto.PaymentDto;
import com.bwongo.core.core_banking.service.MemberService;
import com.bwongo.core.core_banking.service.PaymentService;
import com.bwongo.core.notify_mgt.repository.NotificationRepository;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.school_mgt.repository.SchoolRepository;
import com.bwongo.core.school_mgt.repository.SchoolUserRepository;
import com.bwongo.core.school_mgt.service.SchoolService;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.bwongo.core.account_mgt.utils.AccountMsgConstants.*;
import static com.bwongo.core.base.utils.EnumValidations.isAccountType;
import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AuditService auditService;
    private final TAccountRepository accountRepository;
    private final SchoolUserRepository schoolUserRepository;
    private final ZengaPayApiCall zengaPayApiCall;
    private final TMomoDepositRepository momoDepositRepository;
    private final TAccountTransactionRepository accountTransactionRepository;
    private final TCashFlowRepository cashFlowRepository;
    private final MemberService memberService;
    private final SchoolService schoolService;
    private final SchoolRepository schoolRepository;
    private final PaymentService paymentService;
    private final NotificationRepository notificationRepository;
    private final AccountDtoService accountDtoService;
    private final TLowBalanceNotificationRepository lowBalanceNotificationRepository;

    @Value("${sms-account.notification.balance}")
    private Long accountBalanceNotificationAmount;

    private static final String DEPOSIT_NARRATION = "School sms account deposit";
    private static final String SMS_CHARGE_NOTE = "SMS charge";
    private static final String VENDOR_FAILED_CONNECTION = "Failed to initiate payment: CONNECTION TO TELECOM FAILED";
    private static final String SMS_TOP_UP_ACCOUNT_NUMBER = "SMS256";
    private static final String SMS_COLLECTION_ACCOUNT_NUMBER = "SMS256C";
    private static final String SUCCEEDED = "SUCCEEDED";
    private static final String INDETERMINATE = "INDETERMINATE";
    private static final String FAILED = "FAILED";

    public BigDecimal getAccountBalance(){

        var staffId = auditService.getLoggedInUser().getId();
        var staff = new TUser();
        staff.setId(auditService.getLoggedInUser().getId());

        var existingSchoolUser = schoolUserRepository.findByUser(staff);
        Validate.isPresent(this, existingSchoolUser, SCHOOL_USER_NOT_FOUND, staffId);
        var schoolUser = existingSchoolUser.get();

        var existingSchoolAccount = accountRepository.findBySchool(schoolUser.getSchool());
        Validate.isPresent(this, existingSchoolAccount, SCHOOL_HAS_NO_ACCOUNT);
        var schoolAccount = existingSchoolAccount.get();

        return schoolAccount.getCurrentBalance();
    }

    public BigDecimal getSmsSystemAccountBalance(){
        var existingAccount = accountRepository.findByAccountNumber(SMS_COLLECTION_ACCOUNT_NUMBER);
        Validate.isPresent(this, existingAccount, "Contact Admin");
        var account = existingAccount.get();

        return account.getCurrentBalance();
    }

    @Transactional
    public SmsPaymentResponseDto consumeSmsPayment(SmsPaymentRequestDto smsPaymentRequestDto){

        smsPaymentRequestDto.validate();
        var existingNotification = notificationRepository.findById(smsPaymentRequestDto.notificationId());
        Validate.isPresent(this, existingNotification, NOTIFICATION_NOT_FOUND, smsPaymentRequestDto.notificationId());
        var notification = existingNotification.get();

        final var accountNumber = notification.getAccountNumber();
        final var transactionReference = generatesMSExternalReference();

        var existingSchoolAccount = accountRepository.findByAccountNumber(accountNumber);
        Validate.isPresent(this, existingSchoolAccount, ACCOUNT_NUMBER_NOT_FOUND, accountNumber);
        var schoolAccount = existingSchoolAccount.get();

        var smsCost = schoolAccount.getSchool().getSmsCost();

        var existingSmsCollectionAccount = accountRepository.findByAccountNumber(SMS_COLLECTION_ACCOUNT_NUMBER);
        var smsCollectionAccount = new TAccount();
        if(existingSmsCollectionAccount.isPresent())
            smsCollectionAccount = existingSmsCollectionAccount.get();

        //Credit school account
        var schoolAccountBalanceBeforeCredit = schoolAccount.getCurrentBalance();
        Validate.isTrue(this, schoolAccountBalanceBeforeCredit.compareTo(smsCost) > 0, ExceptionType.BAD_REQUEST, LOW_ACCOUNT_BALANCE, accountNumber);
        var schoolAccountBalanceAfterCredit = schoolAccountBalanceBeforeCredit.subtract(smsCost);

        var schoolAccountTransactionCredit = new TAccountTransaction();
        schoolAccountTransactionCredit.setAccount(schoolAccount);
        schoolAccountTransactionCredit.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        schoolAccountTransactionCredit.setNonReversal(Boolean.TRUE);
        schoolAccountTransactionCredit.setTransactionType(TransactionType.ACCOUNT_CREDIT);
        schoolAccountTransactionCredit.setNote(SMS_CHARGE_NOTE);
        schoolAccountTransactionCredit.setExternalTransactionId(transactionReference);
        schoolAccountTransactionCredit.setBalanceBefore(schoolAccountBalanceBeforeCredit);
        schoolAccountTransactionCredit.setBalanceAfter(schoolAccountBalanceAfterCredit);

        auditService.stampAuditedEntity(schoolAccountTransactionCredit);
        var savedSchoolAccountTransaction = accountTransactionRepository.save(schoolAccountTransactionCredit);

        schoolAccount.setCurrentBalance(schoolAccountBalanceAfterCredit);
        auditService.stampAuditedEntity(schoolAccount);
        accountRepository.save(schoolAccount);

        //Debit sms collection account
        var smsCollectionAccountBalanceBeforeDebit = smsCollectionAccount.getCurrentBalance();
        var smsCollectionAccountBalanceAfterDebit = smsCollectionAccountBalanceBeforeDebit.add(smsCost);

        var smsCollectionAccountTransactionDebit = new TAccountTransaction();
        smsCollectionAccountTransactionDebit.setAccount(smsCollectionAccount);
        smsCollectionAccountTransactionDebit.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        smsCollectionAccountTransactionDebit.setNonReversal(Boolean.TRUE);
        smsCollectionAccountTransactionDebit.setTransactionType(TransactionType.ACCOUNT_DEBIT);
        smsCollectionAccountTransactionDebit.setNote(SMS_CHARGE_NOTE);
        smsCollectionAccountTransactionDebit.setExternalTransactionId(transactionReference);
        smsCollectionAccountTransactionDebit.setBalanceBefore(smsCollectionAccountBalanceBeforeDebit);
        smsCollectionAccountTransactionDebit.setBalanceAfter(smsCollectionAccountBalanceAfterDebit);

        auditService.stampAuditedEntity(smsCollectionAccountTransactionDebit);
        var savedSmsCollectionAccountTransaction = accountTransactionRepository.save(smsCollectionAccountTransactionDebit);

        smsCollectionAccount.setCurrentBalance(smsCollectionAccountBalanceAfterDebit);
        auditService.stampAuditedEntity(smsCollectionAccount);
        accountRepository.save(smsCollectionAccount);

        //Cash flow
        var cashFlow = new TCashFlow();
        cashFlow.setAmount(smsCost);
        cashFlow.setToAccount(smsCollectionAccount);
        cashFlow.setFromAccount(schoolAccount);
        cashFlow.setToAccountTransaction(savedSmsCollectionAccountTransaction);
        cashFlow.setFromAccountTransaction(savedSchoolAccountTransaction);
        cashFlow.setCashFlowType(CashFlowType.BUSINESS_TO_MAIN);

        auditService.stampAuditedEntity(cashFlow);
        cashFlowRepository.save(cashFlow);

        var smsPayment = new PaymentDto();

        smsPayment.setSchoolAccount(schoolAccount.getAccountNumber());
        smsPayment.setStudentSchoolName(schoolAccount.getSchool().getSchoolName());
        smsPayment.setAppRef("SAFE-CHILD-CORE");

        var paymentResult = paymentService.makeCoreBankingSmsPayment(smsPayment);
        var status = paymentResult.getStatus().name();
        Validate.isTrue(this, paymentResult.getStatus().isSuccessful(), ExceptionType.BAD_REQUEST, PAYMENT_FAILED_AT_CORE_BANKING, status);

        return new SmsPaymentResponseDto(
            transactionReference,
            status
        );
    }

    public PaymentResponseDto initiateMomoDeposit(InitiatePaymentRequestDto initiatePaymentRequestDto){

        initiatePaymentRequestDto.validate();

        var staffId = auditService.getLoggedInUser().getId();
        var staff = new TUser();
        staff.setId(staffId);

        var existingSchoolUser = schoolUserRepository.findByUser(staff);
        Validate.isPresent(this, existingSchoolUser, SCHOOL_USER_NOT_FOUND, staffId);
        var schoolUser = existingSchoolUser.get();

        var amount = initiatePaymentRequestDto.amount();
        var msisdn = initiatePaymentRequestDto.phoneNumber();
        var depositorName = initiatePaymentRequestDto.depositorName();

        var collectionRequestDto = new CollectionRequestDto();
        collectionRequestDto.setAmount(amount);
        collectionRequestDto.setMsisdn(msisdn);
        collectionRequestDto.setNarration(DEPOSIT_NARRATION);
        collectionRequestDto.setExternalReference(generateExternalReference());
        collectionRequestDto.setChargeCustomer(Boolean.TRUE);

        var collectionResponseDto = new CollectionResponseDto();
        try {
            collectionResponseDto = zengaPayApiCall.initiatePayment(collectionRequestDto);
        }catch (Exception ex){
            //TODO record failed transaction
            log.error(VENDOR_FAILED_CONNECTION + " : " + ex.getMessage());
            throw new BadRequestException(this, VENDOR_FAILED_CONNECTION);
        }
        //TODO record failed transaction
        Validate.isTrue(this, collectionResponseDto.getCode() == 202, ExceptionType.BAD_REQUEST, collectionResponseDto.getMessage());

        //Persist deposit
        var momoDeposit = new TMomoDeposit();
        momoDeposit.setAmountDeposit(amount);
        momoDeposit.setMsisdn(msisdn);
        momoDeposit.setTransactionStatus(TransactionStatus.PENDING);
        momoDeposit.setExternalReferenceId(collectionResponseDto.getTransactionReference());
        momoDeposit.setDepositorName(depositorName);
        momoDeposit.setSchool(schoolUser.getSchool());
        momoDeposit.setNetworkType(NetworkType.valueOf(initiatePaymentRequestDto.network()));

        auditService.stampAuditedEntity(momoDeposit);
        momoDepositRepository.save(momoDeposit);

        return new PaymentResponseDto(
                TransactionStatus.PENDING,
                collectionResponseDto.getTransactionReference()
        );
    }

    @Transactional
    @Async("asyncTaskExecutor")
    public void updatePendingPaymentDeposits(){
        log.info(Thread.currentThread().getName());
        try {
            List<TMomoDeposit> momoDepositList = momoDepositRepository.findByTransactionStatus(TransactionStatus.PENDING);
            for (TMomoDeposit deposit : momoDepositList) {
                try {
                    checkDepositStatus(deposit);
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public PaymentResponseDto getDepositPaymentStatus(String externalReferenceId){
        var existingMomoDeposit = momoDepositRepository.findByExternalReferenceId(externalReferenceId);
        Validate.isPresent(this, existingMomoDeposit, DEPOSIT_NOT_FOUND, externalReferenceId);
        var momoDeposit = existingMomoDeposit.get();

        return new PaymentResponseDto(
                momoDeposit.getTransactionStatus(),
                momoDeposit.getExternalReferenceId()
        );
    }

    @Transactional
    public void checkDepositStatus(TMomoDeposit momoDeposit) {

        var amount = momoDeposit.getAmountDeposit();
        var transactionReference = momoDeposit.getExternalReferenceId();
        var school = momoDeposit.getSchool();
        var auditUser = momoDeposit.getCreatedBy();

        var statusResponseDto = new StatusResponseDto();
        try {
            statusResponseDto = zengaPayApiCall.getPaymentStatus(transactionReference);
        }catch (Exception ex){
            log.error(VENDOR_FAILED_CONNECTION + " : " + ex.getMessage());
            throw new BadRequestException(this, VENDOR_FAILED_CONNECTION);
        }
        var transactionNote = statusResponseDto.getData().getTransactionStatus();

        if(transactionNote.equals(FAILED) || transactionNote.equals(INDETERMINATE)){
            momoDeposit.setTransactionStatus(TransactionStatus.FAILED);
        }

        if(transactionNote.equals(SUCCEEDED)){
            momoDeposit.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        }
        Validate.isTrue(this, (statusResponseDto.getCode() == 200) && (transactionNote.equals(SUCCEEDED)), ExceptionType.BAD_REQUEST, transactionNote);

        //Persist deposit

        //Credit main sms account with deposited amount
        var existingMainSmsAccount = accountRepository.findByAccountNumber(SMS_TOP_UP_ACCOUNT_NUMBER);
        var mainSmsAccount = new TAccount();
        if(existingMainSmsAccount.isPresent())
            mainSmsAccount = existingMainSmsAccount.get();

        var mainSmsAccountAmountCreditBefore = mainSmsAccount.getCurrentBalance();
        var mainSmsAccountAmountCreditAfter = mainSmsAccountAmountCreditBefore.add(amount);

        var mainAccountTransactionCredit = new TAccountTransaction();
        mainAccountTransactionCredit.setAccount(mainSmsAccount);
        mainAccountTransactionCredit.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        mainAccountTransactionCredit.setNonReversal(Boolean.TRUE);
        mainAccountTransactionCredit.setTransactionType(TransactionType.ACCOUNT_CREDIT);
        mainAccountTransactionCredit.setNote(transactionNote);
        mainAccountTransactionCredit.setExternalTransactionId(transactionReference);
        mainAccountTransactionCredit.setBalanceBefore(mainSmsAccountAmountCreditBefore);
        mainAccountTransactionCredit.setBalanceAfter(mainSmsAccountAmountCreditAfter);

        auditService.stampLongEntity(mainAccountTransactionCredit);
        mainAccountTransactionCredit.setModifiedBy(auditUser);
        mainAccountTransactionCredit.setCreatedBy(auditUser);
        accountTransactionRepository.save(mainAccountTransactionCredit);

        //update main account with deposit amount
        mainSmsAccount.setCurrentBalance(mainSmsAccountAmountCreditAfter);
        auditService.stampLongEntity(mainSmsAccount);
        mainSmsAccount.setCreatedBy(auditUser);
        mainSmsAccount.setModifiedBy(auditUser);
        var updatedMainSmsAccount = accountRepository.save(mainSmsAccount);

        //Move amount from main sms account to school account
        var mainSmsAccountAmountDebitBefore = updatedMainSmsAccount.getCurrentBalance();
        var mainSmsAccountAmountDebitAfter = mainSmsAccountAmountDebitBefore.subtract(amount);

        var mainAccountTransactionDebit = new TAccountTransaction();
        mainAccountTransactionDebit.setAccount(mainSmsAccount);
        mainAccountTransactionDebit.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        mainAccountTransactionDebit.setNonReversal(Boolean.TRUE);
        mainAccountTransactionDebit.setTransactionType(TransactionType.ACCOUNT_DEBIT);
        mainAccountTransactionDebit.setNote(transactionNote);
        mainAccountTransactionDebit.setExternalTransactionId(transactionReference);
        mainAccountTransactionDebit.setBalanceBefore(mainSmsAccountAmountDebitBefore);
        mainAccountTransactionDebit.setBalanceAfter(mainSmsAccountAmountDebitAfter);

        auditService.stampLongEntity(mainAccountTransactionDebit);
        mainAccountTransactionDebit.setModifiedBy(auditUser);
        mainAccountTransactionDebit.setCreatedBy(auditUser);
        var savedMainAccountTransactionDebit = accountTransactionRepository.save(mainAccountTransactionDebit);

        //update main account with debit amount moved to school account
        updatedMainSmsAccount.setCurrentBalance(mainSmsAccountAmountDebitAfter);
        auditService.stampLongEntity(updatedMainSmsAccount);
        updatedMainSmsAccount.setModifiedBy(auditUser);
        updatedMainSmsAccount.setCreatedBy(auditUser);
        accountRepository.save(updatedMainSmsAccount);

        //Credit school account
        var existingSchoolAccount = accountRepository.findBySchool(school);

        var schoolAccount = new TAccount();
        if(existingSchoolAccount.isEmpty())
            schoolAccount = createSchoolAccountIfNotExist(school, auditUser);


        schoolAccount = existingSchoolAccount.get();

        var schoolAccountBalanceCreditBefore = schoolAccount.getCurrentBalance();
        var schoolAccountBalanceCreditAfter = schoolAccountBalanceCreditBefore.add(amount);

        var schoolAccountTransaction = new TAccountTransaction();
        schoolAccountTransaction.setAccount(schoolAccount);
        schoolAccountTransaction.setTransactionStatus(TransactionStatus.SUCCESSFUL);
        schoolAccountTransaction.setNonReversal(Boolean.TRUE);
        schoolAccountTransaction.setTransactionType(TransactionType.ACCOUNT_CREDIT);
        schoolAccountTransaction.setNote(transactionNote);
        schoolAccountTransaction.setExternalTransactionId(transactionReference);
        schoolAccountTransaction.setBalanceBefore(schoolAccountBalanceCreditBefore);
        schoolAccountTransaction.setBalanceAfter(schoolAccountBalanceCreditAfter);

        auditService.stampLongEntity(schoolAccountTransaction);
        schoolAccountTransaction.setModifiedBy(auditUser);
        schoolAccountTransaction.setCreatedBy(auditUser);
        var savedSchoolAccountTransaction = accountTransactionRepository.save(schoolAccountTransaction);

        schoolAccount.setCurrentBalance(schoolAccountBalanceCreditAfter);
        auditService.stampLongEntity(schoolAccount);
        schoolAccount.setModifiedBy(auditUser);
        schoolAccount.setCreatedBy(auditUser);
        accountRepository.save(schoolAccount);

        //RECORD CASH-FLOW
        var cashFlow = new TCashFlow();
        cashFlow.setAmount(amount);
        cashFlow.setToAccount(schoolAccount);
        cashFlow.setFromAccount(mainSmsAccount);
        cashFlow.setToAccountTransaction(savedSchoolAccountTransaction);
        cashFlow.setFromAccountTransaction(savedMainAccountTransactionDebit);
        cashFlow.setCashFlowType(CashFlowType.MAIN_TO_BUSINESS);

        auditService.stampLongEntity(cashFlow);
        cashFlow.setModifiedBy(auditUser);
        cashFlow.setCreatedBy(auditUser);
        cashFlowRepository.save(cashFlow);

        //update momoDeposit
        auditService.stampLongEntity(momoDeposit);
        momoDeposit.setModifiedBy(auditUser);
        momoDepositRepository.save(momoDeposit);

        var momoBankingDto = MomoBankingDto.builder()
                .network(momoDeposit.getNetworkType().name())
                .phoneNumber(momoDeposit.getMsisdn())
                .schoolAccount(schoolAccount.getAccountNumber())
                .amount(amount)
                .build();

        var result = paymentService.makeCoreBakingMomoDeposit(momoBankingDto);

        Validate.isTrue(this, result.getStatus().isSuccessful(), ExceptionType.BAD_REQUEST, result.getStatus().name());
    }

    public void sendLowAccountBalanceNotification(TAccount account){
        var existingBalanceNotification = lowBalanceNotificationRepository.findTopBySchoolAccount(account);

        if(existingBalanceNotification.isPresent()) {

            var lastNotification = existingBalanceNotification.get().getLastNotified();
            var calendar = Calendar.getInstance();
            calendar.setTime(lastNotification);
            calendar.add(Calendar.DATE, 7);

            if (calendar.getTime().compareTo(new Date()) < 0){
                //TODO send email
            }
        }else{
            //TODO send email
        }
    }

    public List<AccountResponseDto> getAccounts(String stringAccountType, Pageable pageable){

        Validate.isTrue(this, isAccountType(stringAccountType), ExceptionType.BAD_REQUEST, INVALID_ACCOUNT_TYPE, stringAccountType);

        var isSchoolAccount = Boolean.FALSE;
        var accountType = AccountType.valueOf(stringAccountType);

        if(accountType.equals(AccountType.SCHOOL))
            isSchoolAccount = Boolean.TRUE;

        return accountRepository.findAllBySchoolAccount(isSchoolAccount, pageable).stream()
                .map(accountDtoService::accountToDto)
                .toList();
    }

    private TAccount createSchoolAccountIfNotExist(TSchool school, TUser auditUser){

        var newAccountNumber = schoolService.getNonExistingSchoolAccountNumber();

        if(school.getAccountNumber() == null/* && school.getCoreBankingId() == null*/){
            school.setAccountNumber(newAccountNumber);
            var coreBankingId = memberService.addSchoolToCoreBanking(school);

            school.setCoreBankingId(coreBankingId);

            auditService.stampLongEntity(school);
            school.setModifiedBy(auditUser);
            schoolRepository.save(school);
        }

        var account = new TAccount();
        account.setSchool(school);
        account.setAccountNumber(newAccountNumber);
        account.setStatus(AccountStatus.ACTIVE);
        account.setSchoolAccount(Boolean.TRUE);
        account.setCurrentBalance(BigDecimal.ZERO);

        auditService.stampLongEntity(account);
        account.setCreatedBy(auditUser);
        account.setModifiedBy(auditUser);
        return accountRepository.save(account);
    }

    private String generateExternalReference(){
        return "NC"+ StringUtil.randomString().toUpperCase().substring(0, 14);
    }

    private String generatesMSExternalReference(){
        return "SMS"+ StringUtil.randomString().toUpperCase().substring(0, 12);
    }
}
