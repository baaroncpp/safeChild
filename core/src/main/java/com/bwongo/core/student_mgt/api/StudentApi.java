package com.bwongo.core.student_mgt.api;

import com.bwongo.core.base.model.dto.PageResponseDto;
import com.bwongo.core.student_mgt.model.dto.*;
import com.bwongo.core.student_mgt.service.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author bkaaron
 * @Project nc
 * @Date 9/28/23
 **/
@Tag(name = "Students",description = "Manage students on SafeChild")
@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentApi {

    private final StudentService studentService;

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('STUDENT_ROLE.WRITE', 'ADMIN_ROLE.WRITE')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentResponseDto addStudent(@RequestBody StudentRequestDto studentRequestDto){
        return studentService.addStudent(studentRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('STUDENT_ROLE.WRITE', 'ADMIN_ROLE.WRITE')")
    @PostMapping(path = "list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudentResponseDto> addStudents(@RequestBody List<StudentRequestDto> studentsRequestDto){
        return studentService.addStudents(studentsRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('STUDENT_ROLE.UPDATE', 'ADMIN_ROLE.UPDATE')")
    @PutMapping(path = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentResponseDto updateStudent(@PathVariable("id") Long id, @RequestBody StudentRequestDto studentRequestDto){
        return studentService.updateStudent(id, studentRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE.READ', 'STUDENT_ROLE.UPDATE')")
    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentResponseDto getStudentById(@PathVariable("id") Long id){
        return studentService.getStudentById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('STUDENT_ROLE.READ', 'ADMIN_ROLE.READ')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentResponseDto getStudentByStudentIdNumberAndSchoolId(@RequestParam("studentIdNumber") String studentIdNumber,
                                                                     @RequestParam("SchoolId") Long schoolId){
        return studentService.getStudentByStudentIdNumberAndSchoolId(studentIdNumber, schoolId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('STUDENT_ROLE.READ', 'ADMIN_ROLE.READ')")
    @GetMapping(path = "all", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseDto getAllStudents(@RequestParam(name = "page", required = true) int page,
                                          @RequestParam(name = "size", required = true) int size,
                                          @RequestParam(name = "schoolId", required = true) Long schoolId){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return studentService.getAllStudents(pageable, schoolId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE.READ')")
    @GetMapping(path = "all-schools", produces = MediaType.APPLICATION_JSON_VALUE)
    public PageResponseDto getAllStudents(@RequestParam(name = "page", required = true) int page,
                                                   @RequestParam(name = "size", required = true) int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return studentService.getAllStudents(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('STUDENT_ROLE.WRITE', 'ADMIN_ROLE.WRITE')")
    @PostMapping(path = "guardian", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentGuardianDto addStudentGuardian(@RequestBody GuardianRequestDto guardianRequestDto){
        return studentService.addStudentGuardian(guardianRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('STUDENT_ROLE.UPDATE', 'ADMIN_ROLE.UPDATE')")
    @PatchMapping(path = "guardian", produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentGuardianDto assignGuardianToStudent(@RequestParam("studentId") Long studentId,
                                                      @RequestParam("guardianId") Long guardianId){
        return studentService.assignGuardianToStudent(studentId, guardianId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('STUDENT_ROLE.READ', 'ADMIN_ROLE.READ')")
    @GetMapping(path = "guardian/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudentGuardianDto> getStudentsByGuardian(@PathVariable("id") Long guardianId){
        return studentService.getStudentsByGuardian(guardianId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('STUDENT_ROLE.READ', 'ADMIN_ROLE.READ')")
    @GetMapping(path = "{id}/guardian", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudentGuardianDto> getGuardiansByStudent(@PathVariable("id") Long studentId){
        return studentService.getGuardiansByStudent(studentId);
    }

    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('STUDENT_ROLE.READ', 'ADMIN_ROLE.READ')")
    @GetMapping(path = "guardian-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GuardianResponseDto getGuardian(@PathVariable("id") Long id){
        return studentService.getGuardianById(id);
    }
}
