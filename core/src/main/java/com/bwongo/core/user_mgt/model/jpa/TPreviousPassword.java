package com.bwongo.core.user_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 11/22/23
 **/
@Entity
@Setter
@Table(name = "t_previous_password", schema = "core")
public class TPreviousPassword extends BaseEntity {
    private TUser user;
    private String previousPassword;
    private int passwordChangeCount;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TUser getUser() {
        return user;
    }

    @Column(name = "previous_password")
    public String getPreviousPassword() {
        return previousPassword;
    }

    @Column(name = "password_change_count")
    public int getPasswordChangeCount() {
        return passwordChangeCount;
    }
}
