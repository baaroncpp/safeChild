package com.bwongo.core.user_mgt.utils;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.enums.UserTypeEnum;
import com.bwongo.core.user_mgt.model.jpa.TPermission;
import com.bwongo.core.user_mgt.model.jpa.TUser;

import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/15/23
 **/
public class UserManagementUtils {
    private UserManagementUtils() {}

    public static boolean checkThatUserIsSchoolUser(TUser user){
        if(user.getUserType().equals(UserTypeEnum.DRIVER) || user.getUserType().equals(UserTypeEnum.SCHOOL_STAFF) || user.getUserType().equals(UserTypeEnum.SCHOOL_ADMIN)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public static void checkThatUserIsAssignable(TUser user){
        Validate.isTrue(UserManagementUtils.class, user.isApproved(), ExceptionType.BAD_REQUEST, USER_ACCOUNT_NOT_APPROVED, user.getId());
        Validate.isTrue(UserManagementUtils.class, !user.getDeleted(), ExceptionType.BAD_REQUEST, USER_ACCOUNT_DELETED);
        Validate.isTrue(UserManagementUtils.class, !user.isAccountExpired(), ExceptionType.BAD_REQUEST, USER_ACCOUNT_EXPIRED);
        Validate.isTrue(UserManagementUtils.class, !user.isCredentialExpired(), ExceptionType.BAD_REQUEST, USER_ACCOUNT_CREDENTIALS_EXPIRED);
        Validate.isTrue(UserManagementUtils.class, !user.isAccountLocked(), ExceptionType.BAD_REQUEST, USER_ACCOUNT_LOCKED);
    }

    public static void checkThatPermissionRoleIsAssignable(TPermission permission){
        Validate.isTrue(UserManagementUtils.class, permission.getAssignable(), ExceptionType.BAD_REQUEST, PERMISSION_IS_IN_ACTIVE);
    }

    public static boolean compareAndUpdateCurrentTUserWithUpdatedTUser(TUser existingUser, TUser updatedUser){
        boolean result = false;

        if(updatedUser.getUsername() != null && (updatedUser.getUsername() != existingUser.getUsername())){
            updatedUser.setUsername(existingUser.getUsername());
            result =    Boolean.TRUE;
        }

        if(updatedUser.getPassword() != null && (updatedUser.getPassword() != existingUser.getPassword())){
            updatedUser.setPassword(existingUser.getPassword());
            result =    Boolean.TRUE;
        }

        if(updatedUser.getUserGroup().getId() != null && (updatedUser.getUserGroup().getId() != existingUser.getUserGroup().getId())){
            updatedUser.setUserGroup(existingUser.getUserGroup());
            result =    Boolean.TRUE;
        }

        if(updatedUser.getUserMetaId() != null && (updatedUser.getUserMetaId() != existingUser.getUserMetaId())){
            updatedUser.setUserMetaId(existingUser.getUserMetaId());
            result =    Boolean.TRUE;
        }

        return result;
    }

}
