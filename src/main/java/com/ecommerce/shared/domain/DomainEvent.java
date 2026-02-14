package com.ecommerce.shared.domain;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredOn();
}
