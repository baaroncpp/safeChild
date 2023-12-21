package com.bwongo.core.account_mgt.api;

import com.bwongo.core.account_mgt.model.dto.SmsPaymentRequestDto;
import com.bwongo.core.account_mgt.model.dto.SmsPaymentResponseDto;
import com.bwongo.core.account_mgt.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/20/23
 **/
@RestController
@RequestMapping("/api/v1/account/service")
@RequiredArgsConstructor
public class AccountServiceApi {

    private final AccountService accountService;

    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE.WRITE')")
    @PostMapping(path = "sms/payment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SmsPaymentResponseDto payForSms(@RequestBody SmsPaymentRequestDto smsPaymentRequestDto){
        return accountService.consumeSmsPayment(smsPaymentRequestDto);
    }
}
