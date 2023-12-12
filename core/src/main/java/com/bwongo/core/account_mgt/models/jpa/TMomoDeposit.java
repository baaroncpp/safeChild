package com.bwongo.core.account_mgt.models.jpa;

import com.bwongo.core.account_mgt.models.enums.TransactionStatus;
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
@Table(name = "t_momo_deposit", schema = "core")
@Setter
public class TMomoDeposit extends AuditEntity {
    private BigDecimal amountDeposit;
    private TransactionStatus transactionStatus;
    private String msisdn;
    private String externalReferenceId;
    private String depositorName;

    @Column(name = "amount_deposit")
    public BigDecimal getAmountDeposit() {
        return amountDeposit;
    }

    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    @Column(name = "msisdn")
    public String getMsisdn() {
        return msisdn;
    }

    @Column(name = "external_reference_id")
    public String getExternalReferenceId() {
        return externalReferenceId;
    }

    @Column(name = "depostor_name")
    public String getDepositorName() {
        return depositorName;
    }
}
