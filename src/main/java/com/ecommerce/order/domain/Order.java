package com.ecommerce.order.domain;

import com.ecommerce.customer.domain.CustomerId;
import com.ecommerce.shared.domain.AggregateRoot;
import java.util.ArrayList;
import java.util.List;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final List<OrderItem> items;
    private OrderStatus status;

    public Order(OrderId id, CustomerId customerId) {
        super(id);
        this.customerId = customerId;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
