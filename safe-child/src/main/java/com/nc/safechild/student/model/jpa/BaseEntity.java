package com.nc.safechild.student.model.jpa;

import jakarta.persistence.*;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/17/23
 **/
@MappedSuperclass
@Setter
public class BaseEntity implements Serializable {
    private Long id;
    private Date createdOn;
    private Date modifiedOn;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedOn() {
        return createdOn;
    }

    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifiedOn() {
        return modifiedOn;
    }
}

