package com.nc.safechild.student.models.jpa;

import com.nc.safechild.base.model.jpa.BaseEntity;
import com.nc.safechild.base.model.jpa.TLocation;
import com.nc.safechild.student.models.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.Setter;

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
                        @UniqueConstraint(columnNames = {"student_status", "student_username", "school_date"})
                })
@Setter
public class StudentDay extends BaseEntity {
    private String fullName;
    private StudentStatus studentStatus;
    private String staffUsername;
    private String studentUsername;
    private String schoolId;
    private Date schoolDate;
    private boolean onTrip;
    private TLocation location;

    @Column(name = "full_name")
    public String getFullName() {
        return fullName;
    }

    @Column(name = "student_status")
    @Enumerated(EnumType.STRING)
    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    @Column(name = "staff_username")
    public String getStaffUsername() {
        return staffUsername;
    }

    @Column(name = "student_username")
    public String getStudentUsername() {
        return studentUsername;
    }

    @Column(name = "school_id")
    public String getSchoolId() {
        return schoolId;
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
