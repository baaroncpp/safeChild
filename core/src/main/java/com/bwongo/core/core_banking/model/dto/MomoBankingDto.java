package com.bwongo.core.core_banking.model.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/14/23
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MomoBankingDto {
    private String schoolAccount;
    private String phoneNumber;
    private String network;
    private BigDecimal amount;
}
