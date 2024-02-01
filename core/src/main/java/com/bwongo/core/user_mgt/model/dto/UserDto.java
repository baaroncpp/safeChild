package com.bwongo.core.user_mgt.model.dto;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/15/23
 **/
public class UserDto {
    private Long id;
    private Date createdOn;
    private Date modifiedOn;
    private String username;
    private String password;
    private boolean accountLocked;
    private boolean accountExpired;
    private boolean credentialExpired;
    private boolean approved;
    private boolean initialPasswordReset;
    private Long userGroupId;
    private Boolean isDeleted;
    private Long approvedBy;
    private String userType;
    private Long userMetaId;
}
