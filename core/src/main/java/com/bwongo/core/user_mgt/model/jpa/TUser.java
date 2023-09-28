package com.bwongo.core.user_mgt.model.jpa;

import com.bwongo.core.base.model.enums.UserTypeEnum;
import com.bwongo.core.base.model.jpa.BaseEntity;
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

    @Column(name = "username")
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "account_locked")
    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    @Column(name = "account_expired")
    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    @Column(name = "cred_expired")
    public boolean isCredentialExpired() {
        return credentialExpired;
    }

    public void setCredentialExpired(boolean credentialExpired) {
        this.credentialExpired = credentialExpired;
    }

    @JoinColumn(name = "user_group_id", referencedColumnName = "id", insertable = true, updatable = false)
    @OneToOne(fetch = FetchType.EAGER)
    public TUserGroup getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(TUserGroup userGroup) {
        this.userGroup = userGroup;
    }

    @Column(name = "approved")
    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Column(name = "is_deleted")
    public boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Column(name = "approved_by")
    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    @Column(name = "initial_password_reset")
    public boolean isInitialPasswordReset() {
        return initialPasswordReset;
    }

    public void setInitialPasswordReset(boolean initialPasswordReset) {
        this.initialPasswordReset = initialPasswordReset;
    }

    @Column(name = "user_type")
    @Enumerated(value = EnumType.STRING)
    public UserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(UserTypeEnum userType) {
        this.userType = userType;
    }

    @Column(name = "user_meta_id")
    public Long getUserMetaId() {
        return userMetaId;
    }

    public void setUserMetaId(Long userMeta) {
        this.userMetaId = userMetaId;
    }
}
