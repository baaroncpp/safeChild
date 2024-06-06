package com.bwongo.core.school_mgt.api;

import com.bwongo.core.base.model.dto.PageResponseDto;
import com.bwongo.core.school_mgt.model.dto.SchoolRequestDto;
import com.bwongo.core.school_mgt.model.dto.SchoolResponseDto;
import com.bwongo.core.school_mgt.service.SchoolService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/7/23
 **/
@Tag(name = "Schools", description = "Manage schools on SafeChild")
@RestController
@RequestMapping("/api/v1/school")
@RequiredArgsConstructor
public class SchoolApi {

    private final SchoolService schoolService;

    @PreAuthorize("hasAnyAuthority('SCHOOL_ROLE.WRITE','ADMIN_ROLE.WRITE')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SchoolResponseDto addSchool(@RequestBody SchoolRequestDto schoolRequestDto) {
        return schoolService.addSchool(schoolRequestDto);
    }

    @PreAuthorize("hasAnyAuthority('SCHOOL_ROLE.UPDATE','ADMIN_ROLE.UPDATE')")
    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public SchoolResponseDto updateSchool(@RequestBody SchoolRequestDto schoolRequestDto,
                                          @PathVariable("id") Long id) {
        return schoolService.updateSchool(id, schoolRequestDto);
    }

    @PreAuthorize("hasAnyAuthority('SCHOOL_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SchoolResponseDto getSchoolById(@PathVariable("id") Long id){
        return schoolService.getSchoolById(id);
    }

    @PreAuthorize("hasAnyAuthority('SCHOOL_ROLE.READ','ADMIN_ROLE.READ')")
    @GetMapping(path = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseDto getSchools(@RequestParam(name = "page", required = true) int page,
                                      @RequestParam(name = "size", required = true) int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return schoolService.getAllSchools(pageable);
    }

    @GetMapping(path = "images/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getSchoolImages(@PathVariable("id") Long id){
        return schoolService.getSchoolImages(id);
    }
}
