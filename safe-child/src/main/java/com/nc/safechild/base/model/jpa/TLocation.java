package com.nc.safechild.base.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Setter;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/25/23
 **/
@Entity
@Table(name = "t_location", schema = "core")
@Setter
public class TLocation extends BaseEntity{
    private double latitude;
    private double longitude;

    @Column(name = "latitude")
    public double getLatitude() {
        return latitude;
    }

    @Column(name = "longitude")
    public double getLongitude() {
        return longitude;
    }
}
