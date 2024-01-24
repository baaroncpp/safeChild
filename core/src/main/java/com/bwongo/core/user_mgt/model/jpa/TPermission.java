package com.bwongo.core.user_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 27/05/2022
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_permission", schema = "core")
public class TPermission extends BaseEntity {
    private TRole role;
    private String name;
    private Boolean isAssignable;


    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)//MANY PERMISSIONS TO ONE ROLE
    public TRole getRole() {
        return role;
    }

    public void setRole(TRole role) {
        this.role = role;
    }

    @Column(name = "permission_name", unique = true, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "is_assignable", nullable = false)
    public Boolean getAssignable() {
        return isAssignable;
    }

    public void setAssignable(Boolean assignable) {
        isAssignable = assignable;
    }
}
