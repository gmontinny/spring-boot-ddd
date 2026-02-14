package com.ecommerce.order.domain;

import com.ecommerce.shared.domain.DomainEvent;
import java.time.Instant;

public record OrderCreatedEvent(OrderId orderId, Instant occurredOn) implements DomainEvent {
    public OrderCreatedEvent(OrderId orderId) {
        this(orderId, Instant.now());
    }
}
