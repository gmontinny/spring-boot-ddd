package com.ecommerce.customer.domain;

import com.ecommerce.shared.domain.ValueObject;
import java.util.UUID;

public class CustomerId extends ValueObject {
    private final UUID value;

    public CustomerId(UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    public static CustomerId generate() {
        return new CustomerId(UUID.randomUUID());
    }
}
