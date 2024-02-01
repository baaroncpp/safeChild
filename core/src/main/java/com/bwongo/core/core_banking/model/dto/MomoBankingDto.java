package com.bwongo.core.core_banking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class MomoBankingDto {
    private String schoolAccount;
    private String phoneNumber;
    private String network;
    private BigDecimal amount;
}
