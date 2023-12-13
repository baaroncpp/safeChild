package com.bwongo.core.account_mgt.event;

import com.bwongo.core.account_mgt.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/13/23
 **/
@RequiredArgsConstructor
public class ScheduledEvents {

    private final AccountService accountService;

    @Scheduled(fixedDelay = 30000)
    public void updatePendingMomoDeposits(){
        accountService.updatePendingPaymentDeposits();
    }
}
