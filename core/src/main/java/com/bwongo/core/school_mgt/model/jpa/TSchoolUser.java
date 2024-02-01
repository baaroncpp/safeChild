package com.bwongo.core.school_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.user_mgt.model.jpa.TUser;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/25/23
 **/
@Entity
@Table(name = "t_school_user", schema = "core")
@Setter
public class TSchoolUser extends AuditEntity {
    private TSchool school;
    private TUser user;

    @JoinColumn(name = "school_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TSchool getSchool() {
        return school;
    }

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TUser getUser() {
        return user;
    }
}
