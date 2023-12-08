package com.bwongo.core.base.api;

import com.bwongo.core.base.model.dto.CountryResponseDto;
import com.bwongo.core.base.model.dto.DistrictResponseDto;
import com.bwongo.core.base.model.dto.LogResponseDto;
import com.bwongo.core.base.service.BaseService;
import com.bwongo.core.base.service.LogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/28/23
 **/
@Tag(name = "Countries and Districts",description = "Manage countries and districts on SafeChild")
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class BaseApi {

    private final BaseService baseService;
    private final LogService logService;

    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE.READ', 'LOCATION_ROLE.READ')")
    @GetMapping(path = "districts", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DistrictResponseDto> getAllDistricts(@RequestParam("page") int page,
                                                     @RequestParam("size") int size,
                                                     @RequestParam("countryId") Long countryId){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return baseService.getAllDistrictByCountryId(countryId, pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE.READ', 'LOCATION_ROLE.READ')")
    @GetMapping(path = "countries", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CountryResponseDto> getAllCountries(){
        return baseService.getAllCountries();
    }

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
