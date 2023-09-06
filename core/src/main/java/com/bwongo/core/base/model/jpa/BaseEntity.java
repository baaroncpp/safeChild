package com.bwongo.core.base.model.jpa;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 27/05/2022
 */
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

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "created_on",insertable =false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
