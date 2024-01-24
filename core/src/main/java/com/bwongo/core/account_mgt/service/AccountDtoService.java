package com.bwongo.core.account_mgt.service;

import com.bwongo.core.account_mgt.model.dto.AccountResponseDto;
import com.bwongo.core.account_mgt.model.jpa.TAccount;
import com.bwongo.core.school_mgt.service.SchoolDtoService;
import com.bwongo.core.user_mgt.service.UserDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/20/23
 **/
@Service
@RequiredArgsConstructor
public class AccountDtoService {

    private final UserDtoService userDtoService;
    private final SchoolDtoService schoolDtoService;

    public AccountResponseDto accountToDto(TAccount account){

        if(account == null){
            return null;
        }

        return new AccountResponseDto(
                account.getId(),
                account.getCreatedOn(),
                account.getModifiedOn(),
                userDtoService.tUserToDto(account.getCreatedBy()),
                userDtoService.tUserToDto(account.getModifiedBy()),
                account.getAccountName(),
                account.getAccountNumber(),
                account.getStatus(),
                schoolDtoService.schoolToDto(account.getSchool()),
                account.getCurrentBalance(),
                account.isSchoolAccount()
        );
    }
}
