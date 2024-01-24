package com.bwongo.core.student_mgt.model.jpa;

import com.bwongo.core.base.model.enums.IdentificationType;
import com.bwongo.core.base.model.enums.Relation;
import com.bwongo.core.base.model.jpa.AuditEntity;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/25/23
 **/
@Entity
@Table(name = "t_guardian", schema = "core")
@Setter
public class TGuardian extends AuditEntity {
    private String fullName;
    private String phoneNumber;
    private String address;
    private Relation relation;
    private IdentificationType identificationType;
    private String idNumber;
    private boolean isNotified;

    @Column(name = "full_name")
    public String getFullName() {
        return fullName;
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    @Column(name = "relation")
    public Relation getRelation() {
        return relation;
    }

    @Column(name = "identification_type")
    public IdentificationType getIdentificationType() {
        return identificationType;
    }

    @Column(name = "id_number")
    public String getIdNumber() {
        return idNumber;
    }

    @Column(name = "is_notified")
    public boolean isNotified() {
        return isNotified;
    }
}
