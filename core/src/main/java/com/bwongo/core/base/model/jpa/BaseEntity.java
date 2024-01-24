package com.bwongo.core.base.model.jpa;

import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 27/05/2022
 */
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {
    private Long id;
    private Date createdOn;
    private Date modifiedOn;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @Column(name = "created_on",insertable =false, updatable = false)
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
