package com.bwongo.core.account_mgt.repository;

import com.bwongo.core.account_mgt.model.enums.TransactionStatus;
import com.bwongo.core.account_mgt.model.jpa.TMomoDeposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
@Repository
public interface TMomoDepositRepository extends JpaRepository<TMomoDeposit, Long> {
    List<TMomoDeposit> findByTransactionStatus(TransactionStatus transactionStatus);
    Optional<TMomoDeposit> findByExternalReferenceId(String externalReferenceId);
}
