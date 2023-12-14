package com.bwongo.core.account_mgt.model.jpa;

import com.bwongo.core.account_mgt.model.enums.NetworkType;
import com.bwongo.core.account_mgt.model.enums.TransactionStatus;
import com.bwongo.core.base.model.jpa.AuditEntity;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import lombok.Setter;

import javax.persistence.*;
import javax.transaction.TransactionScoped;
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
    private TSchool school;
    private NetworkType networkType;

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

    @Column(name = "depositor_name")
    public String getDepositorName() {
        return depositorName;
    }

    @JoinColumn(name = "school_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    public TSchool getSchool() {
        return school;
    }

    @Column(name = "network_type")
    @Enumerated(EnumType.STRING)
    public NetworkType getNetworkType() {
        return networkType;
    }
}
