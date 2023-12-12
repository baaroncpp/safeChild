package com.bwongo.core.account_mgt.models.enums;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/12/23
 **/
public enum CashFlowType {
    MAIN_TO_BUSINESS("Money is leaving main wallet and going to user wallet"),
    STOCK_WITHDRAW("Money is leaving the system entirely"),
    BUSINESS_TO_MAIN("Money is leaving the user wallet and going to main wallet");

    private final String description;
    private CashFlowType(String description){
        this.description = description;
    }
}
