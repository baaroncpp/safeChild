package com.bwongo.core.core_banking.service;

import com.bwongo.core.core_banking.model.dto.MomoBankingDto;
import com.bwongo.core.core_banking.model.dto.PaymentDto;
import nl.strohalm.cyclos.webservices.payments.PaymentResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.bwongo.core.core_banking.utils.CoreBankingWebServiceUtils.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/14/23
 **/
@Service
public class PaymentService {

    @Value("${transfer-type.momo-deposit}")
    private Long momoDepositTransferId;

    @Value("${transfer-type.sms-notification}")
    private Long smsPaymentTransferId;

    @Value("${accounts.sms}")
    private String coreBankingSmsAccount;

    public PaymentResult makeCoreBakingMomoDeposit(MomoBankingDto momoBankingDto){
        return makeMomoDeposit(momoBankingDto, momoDepositTransferId);
    }

    public PaymentResult makeCoreBankingSmsPayment(PaymentDto paymentDto){

        paymentDto.setSmsTransferType(smsPaymentTransferId);
        paymentDto.setMainSmsAccount(coreBankingSmsAccount);

        return makeSmsPayment(paymentDto);
    }

}
