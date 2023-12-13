package com.bwongo.core.account_mgt.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
@Setter
@Getter
public class CollectionResponseDto {
    private int code;
    private String status;
    private String message;
    private String transactionReference;
}
