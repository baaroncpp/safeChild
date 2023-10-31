package com.bwongo.core.student_mgt.model.jpa;

import com.bwongo.core.base.model.enums.StudentStatus;
import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.base.model.jpa.TLocation;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/19/23
 **/
@Entity
@Table(name = "t_student_day",
        schema = "core",
        uniqueConstraints =
                {
                        @UniqueConstraint(columnNames = {"student_status", "student_id", "school_date"})
                })
@Setter
public class StudentDay extends AuditEntity {
    private StudentStatus studentStatus;
    private TUser staff;
    private TStudent student;
    private TSchool school;
    private Date schoolDate;
    private boolean onTrip;
    private TLocation location;

    @Column(name = "student_status")
    @Enumerated(EnumType.STRING)
    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TUser getStaff() {
        return staff;
    }

    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(fetch = FetchType.LAZY)
    public TStudent getStudent() {
        return student;
    }

    @JoinColumn(name = "school_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    public TSchool getSchool() {
        return school;
    }

    @Column(name = "school_date")
    @Temporal(TemporalType.DATE)
    public Date getSchoolDate() {
        return schoolDate;
    }

    @Column(name = "on_trip")
    public boolean isOnTrip() {
        return onTrip;
    }

    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.EAGER)
    public TLocation getLocation() {
        return location;
    }
}
