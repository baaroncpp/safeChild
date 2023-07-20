package com.nc.safechild.student.model.jpa;

import com.nc.safechild.student.model.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.Setter;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/19/23
 **/
@Entity
@Table(name = "t_student_day", schema = "core")
@Setter
public class StudentDay extends BaseEntity{
    private StudentStatus studentStatus;
    private String staffUsername;
    private String studentUsername;
    private String schoolId;
    private Date schoolDate;

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
}
