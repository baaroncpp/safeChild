package com.bwongo.core.base.model.jpa;

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
