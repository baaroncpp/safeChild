package com.bwongo.core.student_mgt.model.jpa;

import com.bwongo.core.base.model.enums.StudentStatus;
import com.bwongo.core.base.model.enums.UserTypeEnum;
import com.bwongo.core.base.model.jpa.BaseEntity;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/6/23
 **/
@Entity
@Table(name = "t_user_student_status_count", schema = "core")
@Setter
public class UserStudentStatusCount extends BaseEntity {
    private Long id;
    private TUser staff;
    private Date date;
    private UserTypeEnum userType;
    private StudentStatus studentStatus;
    private Long dateCount;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TUser getStaff() {
        return staff;
    }

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    public Date getDate() {
        return date;
    }

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    public UserTypeEnum getUserType() {
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
