package com.bwongo.core.base.model.dto;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 8/14/23
 **/
public record DistrictResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        CountryResponseDto country,
        String name,
        String region
) {
}
