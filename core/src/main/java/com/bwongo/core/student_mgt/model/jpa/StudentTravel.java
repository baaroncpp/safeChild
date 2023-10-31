package com.bwongo.core.student_mgt.model.jpa;

import com.bwongo.core.base.model.enums.StudentStatus;
import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.base.model.jpa.TLocation;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.trip_mgt.model.jpa.Trip;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/18/23
 **/
@Entity
@Table(name = "t_student_travel",
        schema = "core",
        uniqueConstraints =
        {
            @UniqueConstraint(columnNames = {"student_id", "student_status", "trip_id"})
        })
@Setter
public class StudentTravel extends AuditEntity {
    private Trip trip;
    private TStudent student;
    private StudentStatus studentStatus;
    private TSchool school;
    private TLocation location;

    @JoinColumn(name = "school_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public TSchool getSchool() {
        return school;
    }

    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    public Trip getTrip() {
        return trip;
    }

    @JoinColumn(name = "student_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TStudent getStudent() {
        return student;
    }

    @Column(name = "student_status")
    @Enumerated(EnumType.STRING)
    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.EAGER)
    public TLocation getLocation() {
        return location;
    }
}
