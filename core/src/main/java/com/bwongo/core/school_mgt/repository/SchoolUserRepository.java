package com.bwongo.core.school_mgt.repository;

import com.bwongo.core.school_mgt.model.jpa.TSchoolUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/25/23
 **/
@Repository
public interface SchoolUserRepository extends JpaRepository<TSchoolUser, Long> {
}
