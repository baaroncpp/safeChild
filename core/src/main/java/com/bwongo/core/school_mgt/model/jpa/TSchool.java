package com.bwongo.core.school_mgt.model.jpa;

import com.bwongo.core.base.model.enums.SchoolCategory;
import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.base.model.jpa.TCountry;
import com.bwongo.core.base.model.jpa.TDistrict;
import com.bwongo.core.base.model.jpa.TLocation;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/7/23
 **/
@Setter
@Entity
@Table(name = "t_school", schema = "core")
@ToString
public class TSchool extends AuditEntity {
    private String schoolName;
    private Long groupId;
    private String username;
    private String email;
    private TCountry country;
    private TDistrict district;
    private String phoneNumber;
    private BigDecimal smsCost;
    private String referenceSchoolId;
    private SchoolCategory schoolCategory;
    private boolean isAssigned;
    private TLocation location;
    private String physicalAddress;
    private Long coreBankingId;

    @Column(name = "school_name")
    public String getSchoolName() {
        return schoolName;
    }

    @Column(name = "group_id")
    public Long getGroupId() {
        return groupId;
    }

    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    @JoinColumn(name = "country_id", referencedColumnName = "id",insertable = true,updatable = true)
    @OneToOne(fetch = FetchType.LAZY)
    public TCountry getCountry() {
        return country;
    }

    @JoinColumn(name = "district_id", referencedColumnName = "id",insertable = true,updatable = true)
    @OneToOne(fetch = FetchType.LAZY)
    public TDistrict getDistrict() {
        return district;
    }

    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Column(name = "sms_cost")
    public BigDecimal getSmsCost() {
        return smsCost;
    }

    @Column(name = "reference_school_id")
    public String getReferenceSchoolId() {
        return referenceSchoolId;
    }

    @Column(name = "school_category")
    @Enumerated(EnumType.STRING)
    public SchoolCategory getSchoolCategory() {
        return schoolCategory;
    }

    @Column(name = "is_assigned")
    public boolean isAssigned() {
        return isAssigned;
    }

    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TLocation getLocation() {
        return location;
    }

    @Column(name = "physical_address")
    public String getPhysicalAddress() {
        return physicalAddress;
    }

    @Column(name = "core_banking_id")
    public Long getCoreBankingId() {
        return coreBankingId;
    }
}
