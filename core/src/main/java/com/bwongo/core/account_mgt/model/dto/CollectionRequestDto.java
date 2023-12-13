package com.bwongo.core.account_mgt.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
@Setter
@Getter
@ToString
public class CollectionRequestDto {
    private String msisdn;
    private BigDecimal amount;
    @JsonProperty("external_reference")
    private String externalReference;
    private String narration;
    @JsonProperty("charge_customer")
    private boolean chargeCustomer;
}
