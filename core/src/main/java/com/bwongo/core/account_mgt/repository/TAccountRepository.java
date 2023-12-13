package com.bwongo.core.account_mgt.repository;

import com.bwongo.core.account_mgt.model.jpa.TAccount;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
@Repository
public interface TAccountRepository extends JpaRepository<TAccount, Long> {
    Optional<TAccount> findBySchool(TSchool school);
    Optional<TAccount> findByAccountNumber(String accountNumber);
}
