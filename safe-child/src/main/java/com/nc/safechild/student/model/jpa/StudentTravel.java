package com.nc.safechild.student.model.jpa;

import com.nc.safechild.student.model.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.Setter;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/18/23
 **/
@Entity
@Table(name = "t_student_travel", schema = "core")
@Setter
public class StudentTravel extends BaseEntity{
    private Trip trip;
    private String studentUsername;
    private String driverUsername;
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

    @Column(name = "driver_username")
    public String getDriverUsername() {
        return driverUsername;
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
