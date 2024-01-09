package com.bwongo.core.account_mgt.model.jpa;

import com.bwongo.core.base.model.jpa.BaseEntity;
import com.bwongo.core.school_mgt.model.jpa.TSchool;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 1/9/24
 **/
@Entity
@Table(name = "t_low_balance_notification")
@Setter
public class TLowBalanceNotification extends BaseEntity {
    private TSchool school;
    private Date lastNotified;
    private BigDecimal amountNotifiedAt;
    private TAccount schoolAccount;


    @JoinColumn(name = "school_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    public TSchool getSchool() {
        return school;
    }

    @Column(name = "last_notified")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastNotified() {
        return lastNotified;
    }

    @Column(name = "amount_notified_at")
    public BigDecimal getAmountNotifiedAt() {
        return amountNotifiedAt;
    }

    @JoinColumn(name = "school_account_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    public TAccount getSchoolAccount() {
        return schoolAccount;
    }
}
