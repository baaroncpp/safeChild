package com.bkbwongo.commons.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author bkaaron
 * @Project kabangali
 * @Date 10/13/22
 */
public class Money extends BigDecimal {

    public Money(BigInteger val) {
        super(val);
    }

    public Money(long val) {
        super(val);
    }

    public boolean isGreaterThanZero(){
        return this.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money money){
        return this.compareTo(money) > 0;
    }

    public boolean isLessThan(Money money){
        return this.compareTo(money) < 0;
    }

    public boolean isLessThanOrEqualTo(Money money)
    {
        return this.compareTo(money) <= 0;
    }

    public boolean isGreaterThanOrEqualTo(Money money){
        return this.compareTo(money) >= 0;
    }
}
