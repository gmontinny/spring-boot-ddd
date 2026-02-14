package com.ecommerce.customer.domain;

import com.ecommerce.shared.domain.AggregateRoot;

public class Customer extends AggregateRoot<CustomerId> {
    private String name;
    private Email email;

    public Customer(CustomerId id, String name, Email email) {
        super(id);
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }
}
