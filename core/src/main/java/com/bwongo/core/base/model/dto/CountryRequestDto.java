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
public record CountryRequestDto(
        String name,
        String isoAlpha2,
        String isoAlpha3,
        Integer countryCode
) {
    public void validate(){
        Validate.notEmpty(name, COUNTRY_NAME_REQUIRED);
        Validate.notEmpty(isoAlpha2, COUNTRY_ISO_ALPHA2_REQUIRED);
        Validate.notEmpty(name, COUNTRY_ISO_ALPHA3_REQUIRED);
        Validate.notNull(countryCode, ExceptionType.BAD_REQUEST, COUNTRY_CODE_REQUIRED);
    }
}
