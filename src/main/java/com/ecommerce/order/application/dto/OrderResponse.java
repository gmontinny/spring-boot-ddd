package com.ecommerce.order.application.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
    UUID id,
    UUID customerId,
    String status,
    List<OrderItemResponse> items
) {
    public record OrderItemResponse(
        UUID productId,
        Integer quantity,
        BigDecimal price
    ) {}
}
