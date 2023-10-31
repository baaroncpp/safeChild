package com.bwongo.core.notify_mgt.model.jpa;

import com.bwongo.core.base.model.enums.SmsStatus;
import com.bwongo.core.base.model.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 6/29/23
 **/
@Entity
@Table(name = "t_notification" , schema = "core")
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Notification extends BaseEntity {
    private Long id;
    private String receiver;
    private String sender;
    private String message;
    private SmsStatus status;
    private String statusNote;
    private String transactionId;
    private String externalTransactionId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    @Column(name = "receiver")
    public String getReceiver() {
        return receiver;
    }

    @Column(name = "sender")
    public String getSender() {
        return sender;
    }

    @Column(name = "message")
    public String getMessage() {
        return message;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public SmsStatus getStatus() {
        return status;
    }

    @Column(name = "status_note")
    public String getStatusNote() {
        return statusNote;
    }

    @Column(name = "transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    @Column(name = "external_transaction_id")
    public String getExternalTransactionId() {
        return externalTransactionId;
    }
}
