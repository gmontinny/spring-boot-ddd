package com.ecommerce.shared.infrastructure;

import com.ecommerce.shared.domain.DomainEvent;

public interface EventPublisher {
    void publish(DomainEvent event);
}
