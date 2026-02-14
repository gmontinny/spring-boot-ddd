package com.ecommerce.order.domain;

import com.ecommerce.shared.domain.ValueObject;
import java.util.UUID;

public class OrderId extends ValueObject {
    private final UUID value;

    public OrderId(UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    public static OrderId generate() {
        return new OrderId(UUID.randomUUID());
    }
}
