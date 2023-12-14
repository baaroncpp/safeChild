package com.bwongo.core.core_banking.service;

import com.bwongo.core.core_banking.model.dto.MomoBankingDto;
import com.bwongo.core.core_banking.utils.CoreBankingWebServiceUtils;
import nl.strohalm.cyclos.webservices.payments.PaymentResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/14/23
 **/
@Service
public class PaymentService {

    @Value("${transfer-type.momo-deposit}")
    private Long momoDepositTransferId;

    public PaymentResult makeCoreBakingMomoDeposit(MomoBankingDto momoBankingDto){
        return CoreBankingWebServiceUtils.makeMomoDeposit(momoBankingDto, momoDepositTransferId);
    }
}
