package com.bwongo.core.user_mgt.model.jpa;

import com.bwongo.core.base.model.enums.ApprovalEnum;
import com.bwongo.core.base.model.jpa.AuditEntity;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 10/06/2022
 */
@Entity
@Table(name = "t_user_approval", schema = "core")
@Setter
public class TUserApproval extends AuditEntity {
    private TUser user;
    private ApprovalEnum status;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TUser getUser() {
        return user;
    }

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    public ApprovalEnum getStatus() {
        return status;
    }

}
