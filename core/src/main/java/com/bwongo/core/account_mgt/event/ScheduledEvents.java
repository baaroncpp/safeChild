package com.bwongo.core.account_mgt.event;

import com.bwongo.core.account_mgt.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/13/23
 **/
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ScheduledEvents {

    private final AccountService accountService;

    @Scheduled(fixedDelay = 60000)
    public void updatePendingMomoDeposits(){
        log.info("Momo deposit transaction update Scheduler");
        accountService.updatePendingPaymentDeposits();
    }

    //TODO runs once a day and put on background thread
    public void notifyUsersWithLowAccountBalance(){

    }
}
