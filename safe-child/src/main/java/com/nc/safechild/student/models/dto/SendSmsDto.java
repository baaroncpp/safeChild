package com.nc.safechild.student.models.dto;

import com.nc.safechild.student.models.enums.StudentStatus;
import lombok.*;

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
@ToString
public class SendSmsDto {
    private String appRef;
    private Long staffUserGroupId;
    private String schoolAccount;
    private String schoolName;
    private StudentStatus studentStatus;
    private String guardianPhoneNumber;
    private String staffUsername;
    private String studentUsername;
}
