package com.nc.safechild.student.models.jpa;

import com.nc.safechild.base.model.jpa.BaseEntity;
import com.nc.safechild.student.models.enums.StudentStatus;
import com.nc.safechild.trip.model.jpa.Trip;
import jakarta.persistence.*;
import lombok.Setter;

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
            @UniqueConstraint(columnNames = {"student_username", "student_status"})
        })
@Setter
public class StudentTravel extends BaseEntity {
    private Trip trip;
    private String studentUsername;
    private StudentStatus studentStatus;
    private String schoolId;

    @JoinColumn(name = "trip_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    public Trip getTrip() {
        return trip;
    }

    @Column(name = "student_username")
    public String getStudentUsername() {
        return studentUsername;
    }

    @Column(name = "student_status")
    @Enumerated(EnumType.STRING)
    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    @Column(name = "school_id")
    public String getSchoolId() {
        return schoolId;
    }
}
