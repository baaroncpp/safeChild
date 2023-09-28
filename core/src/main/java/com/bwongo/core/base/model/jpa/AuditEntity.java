package com.bwongo.core.base.model.jpa;


import com.bwongo.core.user_mgt.model.jpa.TUser;
import lombok.ToString;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 10/06/2022
 */
@MappedSuperclass
@ToString
public class AuditEntity extends BaseEntity{
    private TUser createdBy;
    private TUser modifiedBy;
    private boolean isDeleted;

    @JoinColumn(name = "created_by", referencedColumnName = "id", insertable = true)
    @OneToOne(fetch = FetchType.LAZY)
    public TUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(TUser createdBy) {
        this.createdBy = createdBy;
    }

    @JoinColumn(name = "modified_by", referencedColumnName = "id", insertable = true)
    @OneToOne(fetch = FetchType.LAZY)
    public TUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(TUser modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Column(name = "is_deleted")
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
