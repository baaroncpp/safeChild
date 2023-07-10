package com.nc.safechild.student.model.jpa;

import com.nc.safechild.student.model.enums.StudentStatus;
import com.nc.safechild.student.model.enums.UserType;
import jakarta.persistence.*;
import lombok.Setter;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/6/23
 **/
@Entity
@Table(name = "t_user_student_status_count", schema = "core")
@Setter
public class UserStudentStatusCount {
    private Long id;
    private String username;
    private Date createdOn;
    private Date modifiedOn;
    private Date date;
    private UserType userType;
    private StudentStatus studentStatus;
    private Long dateCount;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedOn() {
        return createdOn;
    }

    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifiedOn() {
        return modifiedOn;
    }

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    public Date getDate() {
        return date;
    }

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    public UserType getUserType() {
        return userType;
    }

    @Column(name = "student_status")
    @Enumerated(EnumType.STRING)
    public StudentStatus getStudentStatus() {
        return studentStatus;
    }

    @Column(name = "date_count")
    public Long getDateCount() {
        return dateCount;
    }
}
