package com.bwongo.core.base.api;

import com.bwongo.core.base.model.dto.CountryResponseDto;
import com.bwongo.core.base.model.dto.DistrictResponseDto;
import com.bwongo.core.base.model.enums.FileDirEnum;
import com.bwongo.core.base.service.BaseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        baseService.uploadFile(file, FileDirEnum.PROFILE);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
