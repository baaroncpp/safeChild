package com.bwongo.core.vehicle_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/29/23
 **/
@Entity
@Table(name = "t_vehicle", schema = "core")
@Setter
public class TVehicle extends AuditEntity {
    private TUser currentDriver;
    private String plateNumber;
    private String vehicleModel;
    private boolean onRoute;
    private TSchool school;
    private int maximumCapacity;

    @JoinColumn(name = "current_driver", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TUser getCurrentDriver() {
        return currentDriver;
    }

    @Column(name = "plate_number")
    public String getPlateNumber() {
        return plateNumber;
    }

    @Column(name = "vehicle_model")
    public String getVehicleModel() {
        return vehicleModel;
    }

    @Column(name = "on_route")
    public boolean isOnRoute() {
        return onRoute;
    }

    @JoinColumn(name = "school_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TSchool getSchool() {
        return school;
    }

    @Column(name = "maximum_capacity")
    public int getMaximumCapacity() {
        return maximumCapacity;
    }
}
