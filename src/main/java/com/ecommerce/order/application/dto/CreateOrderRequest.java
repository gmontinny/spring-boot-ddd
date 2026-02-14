package com.ecommerce.order.application.dto;

import java.util.List;
import java.util.UUID;

public record CreateOrderRequest(
    UUID customerId,
    List<OrderItemRequest> items
) {
    public record OrderItemRequest(
        UUID productId,
        Integer quantity
    ) {}
}
