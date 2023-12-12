package com.bwongo.core.account_mgt.models.jpa;

import com.bwongo.core.account_mgt.models.enums.AccountStatus;
import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import lombok.Setter;

import javax.persistence.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
@Entity
@Table(name = "t_account", schema = "core")
@Setter
public class TAccount extends AuditEntity {
    private String accountName;
    private String accountNumber;
    private AccountStatus status;
    private TSchool school;

    @Column(name = "account_name")
    public String getAccountName() {
        return accountName;
    }

    @Column(name = "account_number")
    public String getAccountNumber() {
        return accountNumber;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public AccountStatus getStatus() {
        return status;
    }

    @JoinColumn(name = "school_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TSchool getSchool() {
        return school;
    }
}
