package com.bwongo.core.base.model.dto;

import com.bwongo.commons.models.exceptions.model.ExceptionType;
import com.bwongo.commons.models.utils.Validate;

import static com.bwongo.core.base.utils.BasicMsgConstants.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public record CountryRequestDto(
        String name,
        String isoAlpha2,
        String isoAlpha3,
        Integer countryCode
) {
    public void validate(){
        Validate.notEmpty(this, name, COUNTRY_NAME_REQUIRED);
        Validate.notEmpty(this, isoAlpha2, COUNTRY_ISO_ALPHA2_REQUIRED);
        Validate.notEmpty(this, name, COUNTRY_ISO_ALPHA3_REQUIRED);
        Validate.notNull(this, countryCode, ExceptionType.BAD_REQUEST, COUNTRY_CODE_REQUIRED);
    }
}
