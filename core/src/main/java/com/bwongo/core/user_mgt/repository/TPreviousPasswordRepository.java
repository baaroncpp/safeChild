package com.bwongo.core.user_mgt.repository;

import com.bwongo.core.user_mgt.model.jpa.TPreviousPassword;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 11/22/23
 **/
@Repository
public interface TPreviousPasswordRepository extends JpaRepository<TPreviousPassword, Long> {
    List<TPreviousPassword> findAllByUser(TUser user);
}
