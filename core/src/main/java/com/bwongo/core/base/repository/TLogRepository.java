package com.bwongo.core.base.repository;

import com.bwongo.core.base.model.jpa.TLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/8/23
 **/
@Repository
public interface TLogRepository extends JpaRepository<TLog, Long> {
    List<TLog> findAllByCreatedOn(Date createdOn, Pageable pageable);
}
