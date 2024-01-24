package com.nc.safechild.student.models.dto;

/**
 * @Author bkaaron
 * @Project cyclos-web-service
 * @Date 6/26/23
 **/
public record StudentResponseDto(
        Long id,
        String name,
        String username,
        String email,
        String guardianName,
        String guardianPhoneNumber,
        String studentClass,
        String studentSchool
) {
}
