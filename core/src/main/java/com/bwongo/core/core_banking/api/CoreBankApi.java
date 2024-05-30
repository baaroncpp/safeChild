package com.bwongo.core.core_banking.api;

import com.bwongo.core.core_banking.model.dto.MomoBankingDto;
import com.bwongo.core.core_banking.model.dto.PaymentDto;
import com.bwongo.core.core_banking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 5/30/24
 * @LocalTime 2:10 PM
 **/
@RestController
@RequestMapping("/api/v1/core")
@RequiredArgsConstructor
public class CoreBankApi {

    private final PaymentService paymentService;

    @PostMapping(path = "pay", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object pay(@RequestBody PaymentDto paymentDto){
        return paymentService.makeCoreBankingSmsPayment(paymentDto);
    }

    @PostMapping(path = "momo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object momoDeposit(@RequestBody MomoBankingDto momoBankingDto){
        return paymentService.makeCoreBakingMomoDeposit(momoBankingDto);
    }
}
