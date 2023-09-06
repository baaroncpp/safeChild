package com.bwongo.core.user_mgt.model.jpa;

import com.bwongo.core.base.model.enums.ApprovalEnum;
import com.bwongo.core.base.model.jpa.AuditEntity;
import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 10/06/2022
 */
@Entity
@Table(name = "t_user_approval", schema = "core")
public class TUserApproval extends AuditEntity {
    private Long userId;
    private ApprovalEnum status;

    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    public ApprovalEnum getStatus() {
        return status;
    }

    public void setStatus(ApprovalEnum status) {
        this.status = status;
    }

}
