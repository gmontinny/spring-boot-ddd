package com.ecommerce.product.domain;

import com.ecommerce.shared.domain.ValueObject;
import java.math.BigDecimal;

public class Money extends ValueObject {
    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }
}
