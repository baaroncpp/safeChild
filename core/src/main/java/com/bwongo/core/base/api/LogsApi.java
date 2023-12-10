package com.bwongo.core.base.api;

import com.bwongo.core.base.model.dto.LogResponseDto;
import com.bwongo.core.base.service.LogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/10/23
 **/
@Tag(name = "System Logs",description = "View all system logs")
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class LogsApi {

    private final LogService logService;

    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE.READ')")
    @GetMapping(path = "logs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LogResponseDto> getLogs(@RequestParam("page") int page,
                                        @RequestParam("size") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return logService.getAllLogs(pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE.READ')")
    @GetMapping(path = "logs/date", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LogResponseDto> getLogsByDate(@RequestParam("page") int page,
                                              @RequestParam("size") int size,
                                              @RequestParam("date") String date){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return logService.getLogsByDate(pageable, date);
    }

}
