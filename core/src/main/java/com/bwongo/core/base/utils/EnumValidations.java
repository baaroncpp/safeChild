package com.bwongo.core.base.utils;

import com.bwongo.core.account_mgt.model.enums.AccountType;
import com.bwongo.core.account_mgt.model.enums.NetworkType;
import com.bwongo.core.base.model.enums.*;
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
        List<String> userTypeEnumList = List.of(
                UserTypeEnum.ADMIN.name()
        );
        return userTypeEnumList.contains(value);

    }

    public static boolean isSchoolUserType(String value){
        List<String> userTypeEnumList = Arrays.asList(
                UserTypeEnum.DRIVER.name(),
                UserTypeEnum.SCHOOL_ADMIN.name(),
                UserTypeEnum.SCHOOL_STAFF.name()
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

    public static boolean isSchoolCategory(String value){
        List<String> schoolCategoryList = Arrays.asList(
                SchoolCategory.PRIMARY.name(),
                SchoolCategory.DAY_CARE.name(),
                SchoolCategory.PRE_SCHOOL.name(),
                SchoolCategory.NURSERY.name(),
                SchoolCategory.KINDERGARTEN.name()

        );
        return schoolCategoryList.contains(value);
    }

    public static boolean isRelation(String value){
        List<String> relationList = Arrays.asList(
                Relation.FATHER.name(),
                Relation.MOTHER.name(),
                Relation.BROTHER.name(),
                Relation.SISTER.name(),
                Relation.UNCLE.name(),
                Relation.AUNT.name(),
                Relation.OTHERS.name()

        );
        return relationList.contains(value);
    }

    public static boolean isTripType(String value){
        List<String> tripTypeList = Arrays.asList(
                TripType.PICK_UP.name(),
                TripType.DROP_OFF.name()
        );

        return tripTypeList.contains(value);
    }

    public static boolean isStudentStatus(String value){
        List<String> studentStatusList = Arrays.asList(
                StudentStatus.SCHOOL_SIGN_OUT.name(),
                StudentStatus.HOME_PICK_UP.name(),
                StudentStatus.SCHOOL_SIGN_IN.name(),
                StudentStatus.HOME_DROP_OFF.name(),
                StudentStatus.IN_CLASS.name()
        );
        return studentStatusList.contains(value);
    }

    public static boolean isNetworkType(String value){
        List<String> networkTypeList = Arrays.asList(
                NetworkType.MTN.name(),
                NetworkType.AIRTEL.name()
        );
        return networkTypeList.contains(value);
    }

    public static boolean isAccountType(String value){
        List<String> accountTypeList = Arrays.asList(
                AccountType.SCHOOL.name(),
                AccountType.SYSTEM.name()
        );
        return accountTypeList.contains(value);
    }
}
