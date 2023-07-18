package com.nc.safechild.student.model.jpa;

import com.nc.safechild.student.model.enums.TripStatus;
import com.nc.safechild.student.model.enums.TripType;
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
public class Trip extends BaseEntity{
    private String driverUsername;
    private String schoolId;
    private TripType tripType;
    private TripStatus tripStatus;
    private String note;

    @Column(name = "driver_username")
    public String getDriverUsername() {
        return driverUsername;
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
