package com.bwongo.core.base.model.dto;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/25/23
 **/
public record LocationResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        double latitude,
        double longitude
) {
}
