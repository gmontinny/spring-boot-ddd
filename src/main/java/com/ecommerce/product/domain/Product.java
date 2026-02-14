package com.ecommerce.product.domain;

import com.ecommerce.shared.domain.AggregateRoot;

public class Product extends AggregateRoot<ProductId> {
    private String name;
    private Money price;

    public Product(ProductId id, String name, Money price) {
        super(id);
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
