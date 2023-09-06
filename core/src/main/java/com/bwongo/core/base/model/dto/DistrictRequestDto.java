package com.bwongo.core.base.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;

import java.util.Date;

import static com.bwongo.core.base.utils.BasicMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public record DistrictRequestDto(
          Long id,
          Date createdOn,
          Date modifiedOn,
          Long countryId,
          String name,
          String region
) {
    public void validate(){
        Validate.notEmpty(name, DISTRICT_NAME_REQUIRED);
        Validate.notEmpty(region, DISTRICT_REGION_REQUIRED);
        Validate.notNull(countryId, ExceptionType.BAD_REQUEST,COUNTRY_ID_REQUIRED);
    }
}
