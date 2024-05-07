package com.bwongo.core.base.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 5/7/24
 * @LocalTime 9:04 AM
 **/
@Data
@Builder
public class PageResponseDto {
    private List<?> data;
    private int pageNumber;
    private int totalPages;
    private int pageSize;
    private Long totalElementsCount;
}
