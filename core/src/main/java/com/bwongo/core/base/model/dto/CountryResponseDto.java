package com.bwongo.core.base.model.dto;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public record CountryResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        String name,
        String isoAlpha2,
        String isoAlpha3,
        Integer countryCode
) {
}
