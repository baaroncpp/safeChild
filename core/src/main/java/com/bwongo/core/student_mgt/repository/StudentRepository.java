package com.bwongo.core.student_mgt.repository;

import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
@Repository
public interface StudentRepository extends JpaRepository<TStudent, Long> {
    boolean existsBySchoolIdNumberAndSchool(String schoolIdNumber, TSchool school);
    boolean existsByNationalIdNumber(String nationalIdNumber);
    Optional<TStudent> findByDeletedAndId(boolean isDeleted, Long id);
    Optional<TStudent> findByDeletedAndSchoolIdNumberAndSchool(boolean isDeleted, String schoolIdNumber, TSchool school);
    List<TStudent> findAllByDeletedAndSchool(Pageable pageable, boolean isDeleted, TSchool school);
}
