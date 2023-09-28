package com.bwongo.core.student_mgt.repository;

import com.bwongo.core.student_mgt.model.jpa.TGuardian;
import com.bwongo.core.student_mgt.model.jpa.TStudent;
import com.bwongo.core.student_mgt.model.jpa.TStudentGuardian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
@Repository
public interface StudentGuardianRepository extends JpaRepository<TStudentGuardian, Long> {
    List<TStudentGuardian> findAllByStudent(TStudent student);
    List<TStudentGuardian> findAllByGuardian(TGuardian guardian);
}
