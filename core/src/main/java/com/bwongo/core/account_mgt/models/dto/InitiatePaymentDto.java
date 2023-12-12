package com.bwongo.core.account_mgt.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
@Setter
@Getter
public class InitiatePaymentDto {
    private String apikey;
    private String action;
    private String phone;
    private BigDecimal amount;
}
