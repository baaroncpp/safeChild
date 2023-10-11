package com.bwongo.core.trip_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.BaseEntity;
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
public class Trip extends BaseEntity {
    private String staffUsername;
    private String schoolId;
    private TripType tripType;
    private TripStatus tripStatus;
    private String note;

    @Column(name = "staff_username")
    public String getStaffUsername() {
        return staffUsername;
    }

    @Column(name = "school_id")
    public String getSchoolId() {
        return schoolId;
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
