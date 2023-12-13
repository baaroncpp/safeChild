package com.bwongo.core.account_mgt.model.jpa;

import com.bwongo.core.account_mgt.model.enums.AccountStatus;
import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

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
    private BigDecimal currentBalance;
    private boolean isSchoolAccount;

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

    @Column(name = "current_balance")
    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    @Column(name = "is_school_account")
    public boolean isSchoolAccount() {
        return isSchoolAccount;
    }
}
