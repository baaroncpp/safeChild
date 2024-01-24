package com.bwongo.core.account_mgt.api;

import com.bwongo.core.account_mgt.model.dto.AccountResponseDto;
import com.bwongo.core.account_mgt.model.dto.InitiatePaymentRequestDto;
import com.bwongo.core.account_mgt.model.dto.PaymentResponseDto;
import com.bwongo.core.account_mgt.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/13/23
 **/
@Tag(name = "School Accounts",description = "School accounts management")
@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountApi {

    private final AccountService accountService;

    @PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE.WRITE')")
    @PostMapping(path ="initiate/momo/deposit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PaymentResponseDto initiatePayment(@RequestBody InitiatePaymentRequestDto initiatePaymentRequestDto){
        return accountService.initiateMomoDeposit(initiatePaymentRequestDto);
    }

    @PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE.READ', 'ADMIN_ROLE.READ')")
    @GetMapping(path = "/deposit/status/{transactionReferenceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PaymentResponseDto getDepositStatus(@PathVariable("transactionReferenceId") String transactionReferenceId){
        return accountService.getDepositPaymentStatus(transactionReferenceId);
    }

    @PreAuthorize("hasAnyAuthority('ACCOUNT_ROLE.READ', 'ADMIN_ROLE.READ')")
    @GetMapping(path = "account/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public BigDecimal getSchoolAccountBalance(){
        return accountService.getAccountBalance();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE.READ')")
    @GetMapping(path = "account/{accountNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BigDecimal getSchoolAccountBalanceByAccountNumber(@PathVariable("accountNumber") String accountNumber){
        return accountService.getAccountBalance();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE.READ')")
    @GetMapping(path = "all/by/type", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccountResponseDto> getAllSchoolAccounts(@RequestParam(name = "page", required = true) int page,
                                                         @RequestParam(name = "size", required = true) int size,
                                                         @RequestParam(name = "accountType", required = true) String accountType){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return accountService.getAccounts(accountType, pageable);
    }
}
