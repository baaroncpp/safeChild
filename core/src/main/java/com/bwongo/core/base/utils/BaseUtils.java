package com.bwongo.core.base.utils;

import com.bwongo.core.base.model.dto.PageResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 5/7/24
 * @LocalTime 9:48 AM
 **/
public class BaseUtils {

    private BaseUtils() { }

    public static PageResponseDto pageToDto(Page<?> page, List<?> data) {
        var totalPages = page.getTotalPages();
        var number = page.getNumber();
        var size = page.getSize();
        var totalElements = page.getTotalElements();

        return PageResponseDto.builder()
                .pageNumber(number)
                .pageSize(size)
                .totalElementsCount(totalElements)
                .totalPages(totalPages)
                .data(data)
                .build();
    }
}
