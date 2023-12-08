package com.bwongo.core.base.model.dto;

import com.bwongo.core.base.model.enums.LogLevelEnum;

import java.util.Date;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/8/23
 **/
public record LogResponseDto(
        Long id,
        Date createdOn,
        Date modifiedOn,
        String resourceUrl,
        String httpStatus,
        LogLevelEnum logLevel,
        String note,
        String entityName
) {
}
