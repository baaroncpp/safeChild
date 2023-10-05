package com.bwongo.core.student_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/25/23
 **/
@Entity
@Table(name = "t_student", schema = "core")
@Setter
public class TStudent extends AuditEntity {
    private String firstName;
    private String secondName;
    private String schoolIdNumber;
    private String nationalIdNumber;
    private String email;
    private String studentClass;
    private TSchool school;
    private String profileImagePathUrl;
    private String idImagePathUrl;
    private boolean canBeNotified;
    private Long coreBankingId;
    private String physicalAddress;

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "second_name")
    public String getSecondName() {
        return secondName;
    }

    @Column(name = "school_id_number")
    public String getSchoolIdNumber() {
        return schoolIdNumber;
    }

    @Column(name = "national_id_number")
    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    @Column(name = "student_class")
    public String getStudentClass() {
        return studentClass;
    }

    @JoinColumn(name = "school_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TSchool getSchool() {
        return school;
    }

    @Column(name = "profile_image_path_url")
    public String getProfileImagePathUrl() {
        return profileImagePathUrl;
    }

    @Column(name = "id_image_path_url")
    public String getIdImagePathUrl() {
        return idImagePathUrl;
    }

    @Column(name = "can_be_notified")
    public boolean isCanBeNotified() {
        return canBeNotified;
    }

    @Column(name = "core_banking_id")
    public Long getCoreBankingId() {
        return coreBankingId;
    }

    @Column(name = "physical_address")
    public String getPhysicalAddress() {
        return physicalAddress;
    }
}
