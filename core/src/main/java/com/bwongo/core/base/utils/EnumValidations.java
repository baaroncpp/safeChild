package com.bwongo.core.base.utils;

import com.bwongo.core.base.model.enums.ApprovalEnum;
import com.bwongo.core.base.model.enums.IdentificationEnum;
import com.bwongo.core.base.model.enums.IdentificationType;
import com.bwongo.core.base.model.enums.UserTypeEnum;
import com.bwongo.core.user_mgt.model.jpa.TPermission;
import com.bwongo.core.user_mgt.model.jpa.TRole;

import java.util.Arrays;
import java.util.List;

import static com.bwongo.core.user_mgt.utils.UserMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/15/23
 **/
public class EnumValidations {

    public static List<TPermission> isRolePermissions(TRole role){

        String roleName = role.getName();
        return List.of(
                createPermission(role, roleName.concat(READ_PERMISSION)),
                createPermission(role, roleName.concat(WRITE_PERMISSION)),
                createPermission(role, roleName.concat(UPDATE_PERMISSION)),
                createPermission(role, roleName.concat(DELETE_PERMISSION))
        );
    }

    private static TPermission createPermission(TRole role, String permission){

        return TPermission.builder()
                .isAssignable(Boolean.TRUE)
                .role(role)
                .name(permission)
                .build();
    }

    public static boolean isUserType(String value){
        List<String> userTypeEnumList = Arrays.asList(
                UserTypeEnum.DRIVER.name(),
                UserTypeEnum.SYSTEM.name(),
                UserTypeEnum.ADMIN.name(),
                UserTypeEnum.CUSTOMER.name(),
                UserTypeEnum.CLIENT.name()
        );
        return userTypeEnumList.contains(value);

    }

    public static boolean isApprovalStatus(String value){
        List<String> approvalEnumList = Arrays.asList(
                ApprovalEnum.APPROVED.name(),
                ApprovalEnum.REJECTED.name(),
                ApprovalEnum.PENDING.name()
        );
        return approvalEnumList.contains(value);
    }

    public static boolean isIdentificationType(String value){
        List<String> approvalEnumList = Arrays.asList(
                IdentificationType.OTHER.name(),
                IdentificationType.DRIVING_PERMIT.name(),
                IdentificationType.PASSPORT.name(),
                IdentificationType.NATIONAL_ID.name()
        );
        return approvalEnumList.contains(value);
    }

    public static boolean isIdentificationEnum(String value){
        List<String> identificationEnumList = Arrays.asList(
                IdentificationEnum.DRIVING_PERMIT.name(),
                IdentificationEnum.PASSPORT.name(),
                IdentificationEnum.NATIONAL_ID.name()
        );
        return identificationEnumList.contains(value);
    }
}
