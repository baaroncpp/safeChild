package com.nc.safechild.student.models.dto;

import com.nc.safechild.student.models.enums.StudentStatus;
import lombok.*;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 7/23/23
 **/
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDto {
    private String studentSchoolName;
    private String staffUsername;
    private String appRef;
    private StudentStatus studentStatus;
    private String schoolAccount;
    private String mainSmsAccount;
    private Long smsTransferType;
}
