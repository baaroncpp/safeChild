package com.nc.safechild.trip.model.jpa;

import com.nc.safechild.trip.model.enums.TripStatus;
import com.nc.safechild.trip.model.enums.TripType;
import com.nc.safechild.base.model.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/17/23
 **/
@Entity
@Table(name = "t_trip", schema = "core")
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
