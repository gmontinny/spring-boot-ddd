package com.ecommerce.order.domain;

import com.ecommerce.product.domain.ProductId;
import com.ecommerce.shared.domain.Entity;
import java.math.BigDecimal;
import java.util.UUID;

public class OrderItem extends Entity<UUID> {
    private final ProductId productId;
    private final Integer quantity;
    private final BigDecimal price;

    public OrderItem(UUID id, ProductId productId, Integer quantity, BigDecimal price) {
        super(id);
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public ProductId getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
