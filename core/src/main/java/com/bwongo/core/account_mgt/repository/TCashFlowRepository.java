package com.bwongo.core.account_mgt.repository;

import com.bwongo.core.account_mgt.models.jpa.TCashFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
@Repository
public interface TCashFlowRepository extends JpaRepository<TCashFlow, Long> {
}
