package com.bwongo.core.account_mgt.model.jpa;

import com.bwongo.core.account_mgt.model.enums.TransactionStatus;
import com.bwongo.core.account_mgt.model.enums.TransactionType;
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
@Table(name = "t_account_transaction", schema = "core")
@Setter
public class TAccountTransaction extends AuditEntity {
    private TAccount account;
    private TransactionType transactionType;
    private boolean nonReversal;
    private TransactionStatus transactionStatus;
    private String note;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String externalTransactionId;

    @JoinColumn(name = "account_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TAccount getAccount() {
        return account;
    }

    @Column(name = "transaction_type")
    @Enumerated(EnumType.STRING)
    public TransactionType getTransactionType() {
        return transactionType;
    }

    @Column(name = "non_reversal")
    public boolean isNonReversal() {
        return nonReversal;
    }

    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    @Column(name = "note")
    public String getNote() {
        return note;
    }

    @Column(name = "balance_before")
    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    @Column(name = "balance_after")
    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    @Column(name = "external_transaction_id")
    public String getExternalTransactionId() {
        return externalTransactionId;
    }
}
