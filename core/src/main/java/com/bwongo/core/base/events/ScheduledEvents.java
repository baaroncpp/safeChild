package com.bwongo.core.base.events;

import com.bwongo.core.account_mgt.service.AccountService;
import com.bwongo.core.trip_mgt.service.TripService;
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
    private final TripService tripService;

    @Scheduled(fixedDelay = 60000/*, initialDelay = 60000*/)
    public void updatePendingMomoDeposits(){
        log.info("Momo deposit transaction update Scheduler");
        accountService.updatePendingPaymentDeposits();
    }

    @Scheduled(cron="0 0 0 * * *")
    public void endAllOpenAndInProgressTripsAtMidnight(){
        try {
            tripService.endAllOpenTrips();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
