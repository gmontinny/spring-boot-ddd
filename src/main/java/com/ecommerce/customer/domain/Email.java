package com.ecommerce.customer.domain;

import com.ecommerce.shared.domain.ValueObject;

public class Email extends ValueObject {
    private final String address;

    public Email(String address) {
        if (address == null || !address.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
