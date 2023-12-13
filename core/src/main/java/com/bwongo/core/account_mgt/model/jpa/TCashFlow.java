package com.bwongo.core.account_mgt.model.jpa;

import com.bwongo.core.account_mgt.model.enums.CashFlowType;
import com.bwongo.core.base.model.jpa.AuditEntity;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
@Entity
@Table(name = "t_cash_flow", schema = "core")
@Setter
public class TCashFlow extends AuditEntity {
    private BigDecimal amount;
    private TAccountTransaction fromAccountTransaction;
    private TAccountTransaction toAccountTransaction;
    private TAccount fromAccount;
    private TAccount toAccount;
    private CashFlowType cashFlowType;

    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    @JoinColumn(name = "from_account_transaction_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TAccountTransaction getFromAccountTransaction() {
        return fromAccountTransaction;
    }

    @JoinColumn(name = "to_account_transaction_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TAccountTransaction getToAccountTransaction() {
        return toAccountTransaction;
    }

    @JoinColumn(name = "from_account", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TAccount getFromAccount() {
        return fromAccount;
    }

    @JoinColumn(name = "to_account", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TAccount getToAccount() {
        return toAccount;
    }

    @Column(name = "cash_flow_type")
    @Enumerated(EnumType.STRING)
    public CashFlowType getCashFlowType() {
        return cashFlowType;
    }
}
