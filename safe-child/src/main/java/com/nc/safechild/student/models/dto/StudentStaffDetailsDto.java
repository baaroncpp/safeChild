package com.nc.safechild.student.models.dto;

import lombok.*;
import nl.strohalm.cyclos.webservices.model.MemberVO;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/23/23
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentStaffDetailsDto {
    private MemberVO staffUser;
    private MemberVO studentUser;
    private String staffSchoolId;
    private String studentSchoolId;
    private String guardianPhoneNumber;
    private String schoolName;
    private String accountNumber;

}
