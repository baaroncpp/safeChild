package com.bwongo.core.user_mgt.model.jpa;

import com.bwongo.core.base.model.enums.UserTypeEnum;
import com.bwongo.core.base.model.jpa.BaseEntity;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 27/05/2022
 */
@Entity
@Table(name = "t_user", schema = "core")
@ToString
@Setter
public class TUser extends BaseEntity {
    private String username;
    private String password;
    private boolean accountLocked;
    private boolean accountExpired;
    private boolean credentialExpired;
    private boolean approved;
    private boolean initialPasswordReset;
    private TUserGroup userGroup;
    private boolean isDeleted;
    private Long approvedBy;
    private UserTypeEnum userType;
    private Long userMetaId;
    private Long coreBankingId;

    @Column(name = "username")
    public String getUsername() {
        return this.username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    @Column(name = "account_locked")
    public boolean isAccountLocked() {
        return accountLocked;
    }

    @Column(name = "account_expired")
    public boolean isAccountExpired() {
        return accountExpired;
    }

    @Column(name = "cred_expired")
    public boolean isCredentialExpired() {
        return credentialExpired;
    }

    @JoinColumn(name = "user_group_id", referencedColumnName = "id", insertable = true, updatable = false)
    @OneToOne(fetch = FetchType.EAGER)
    public TUserGroup getUserGroup() {
        return userGroup;
    }

    @Column(name = "approved")
    public boolean isApproved() {
        return approved;
    }

    @Column(name = "is_deleted")
    public boolean getDeleted() {
        return isDeleted;
    }

    @Column(name = "approved_by")
    public Long getApprovedBy() {
        return approvedBy;
    }

    @Column(name = "initial_password_reset")
    public boolean isInitialPasswordReset() {
        return initialPasswordReset;
    }

    @Column(name = "user_type")
    @Enumerated(value = EnumType.STRING)
    public UserTypeEnum getUserType() {
        return userType;
    }

    @Column(name = "user_meta_id")
    public Long getUserMetaId() {
        return userMetaId;
    }

    @Column(name = "core_banking_id")
    public Long getCoreBankingId() {
        return coreBankingId;
    }
}
