package com.bwongo.core.user_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.BaseEntity;
import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 27/05/2022
 */
@Entity
@Table(name = "t_group_authority",
       schema = "core",
       uniqueConstraints = {@UniqueConstraint(/*name = "user_group_permission",*/
                                              columnNames = { "user_group_id", "permission_id" })})
public class TGroupAuthority extends BaseEntity {
    private TUserGroup userGroup;
    private TPermission permission;

    @JoinColumn(name = "user_group_id", referencedColumnName = "id", insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    public TUserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(TUserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @JoinColumn(name = "permission_id", referencedColumnName = "id", insertable = true, updatable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    public TPermission getPermission() {
        return permission;
    }

    public void setPermission(TPermission permission) {
        this.permission = permission;
    }

}
