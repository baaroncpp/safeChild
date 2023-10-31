package com.bwongo.core.trip_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.base.model.jpa.BaseEntity;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.bwongo.core.base.model.enums.*;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/17/23
 **/
@Entity
@Table(name = "t_trip", schema = "core")
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trip extends AuditEntity {
    private TUser schoolStaff;
    private TSchool school;
    private TripType tripType;
    private TripStatus tripStatus;
    private String note;

    @JoinColumn(name = "staff_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TUser getSchoolStaff() {
        return schoolStaff;
    }

    @JoinColumn(name = "school_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TSchool getSchool() {
        return school;
    }

    @Column(name = "trip_type")
    @Enumerated(EnumType.STRING)
    public TripType getTripType() {
        return tripType;
    }

    @Column(name = "trip_status")
    @Enumerated(EnumType.STRING)
    public TripStatus getTripStatus() {
        return tripStatus;
    }

    @Column(name = "note")
    public String getNote() {
        return note;
    }
}
