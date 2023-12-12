package com.bwongo.core.account_mgt.service;

import com.bwongo.core.base.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AuditService auditService;
    private final AccountService accountService;

    public Object makeMomoDeposit(){
        return null;
    }
}
