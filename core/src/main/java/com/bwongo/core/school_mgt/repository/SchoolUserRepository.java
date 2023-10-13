package com.bwongo.core.school_mgt.repository;

import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.school_mgt.model.jpa.TSchoolUser;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/25/23
 **/
@Repository
public interface SchoolUserRepository extends JpaRepository<TSchoolUser, Long> {
    boolean existsBySchoolAndUser(TSchool school, TUser user);
    List<TSchoolUser> findAllBySchool(TSchool school);

    Optional<TSchoolUser> findByUser(TUser user);
}
