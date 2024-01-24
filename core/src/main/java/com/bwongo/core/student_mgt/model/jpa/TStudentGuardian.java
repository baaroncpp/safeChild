package com.bwongo.core.student_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.AuditEntity;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/27/23
 **/
@Entity
@Table(name = "t_guardian_student", schema = "core")
@Setter
public class TStudentGuardian extends AuditEntity {
    private TStudent student;
    private TGuardian guardian;

    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TStudent getStudent() {
        return student;
    }

    @JoinColumn(name = "guardian_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TGuardian getGuardian() {
        return guardian;
    }
}
