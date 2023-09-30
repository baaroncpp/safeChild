package com.bwongo.core.vehicle_mgt.utils;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.enums.UserTypeEnum;
import com.bwongo.core.user_mgt.model.jpa.TUser;

import static com.bwongo.core.user_mgt.utils.UserManagementUtils.checkThatUserIsAssignable;
import static com.bwongo.core.vehicle_mgt.utils.VehicleMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/29/23
 **/
public class VehicleUtils {

    private VehicleUtils() { }

    public static void  checkIfUserCanBeDriver(TUser user){
        checkThatUserIsAssignable(user);
        Validate.isTrue(user.getUserType().equals(UserTypeEnum.DRIVER), ExceptionType.BAD_REQUEST, USER_NOT_DRIVER, user.getId());
    }

}
