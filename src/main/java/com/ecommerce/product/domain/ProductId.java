package com.ecommerce.product.domain;

import com.ecommerce.shared.domain.ValueObject;
import java.util.UUID;

public class ProductId extends ValueObject {
    private final UUID value;

    public ProductId(UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }
}
