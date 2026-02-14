package com.ecommerce.order.application;

import com.ecommerce.customer.domain.CustomerId;
import com.ecommerce.customer.domain.CustomerRepository;
import com.ecommerce.order.application.dto.CreateOrderRequest;
import com.ecommerce.order.domain.Order;
import com.ecommerce.order.domain.OrderCreatedEvent;
import com.ecommerce.order.domain.OrderId;
import com.ecommerce.order.domain.OrderItem;
import com.ecommerce.order.domain.OrderRepository;
import com.ecommerce.product.domain.ProductId;
import com.ecommerce.product.domain.ProductRepository;
import com.ecommerce.shared.application.UseCase;
import com.ecommerce.shared.infrastructure.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase implements UseCase<CreateOrderRequest, OrderId> {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public OrderId execute(CreateOrderRequest request) {
        var customerId = new CustomerId(request.customerId());
        customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        var order = new Order(OrderId.generate(), customerId);

        for (var itemRequest : request.items()) {
            var productId = new ProductId(itemRequest.productId());
            var product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.productId()));

            var orderItem = new OrderItem(
                    UUID.randomUUID(),
                    productId,
                    itemRequest.quantity(),
                    product.getPrice().getAmount()
            );
            order.addItem(orderItem);
        }

        orderRepository.save(order);

        // Disparar evento
        var event = new OrderCreatedEvent(order.getId());
        eventPublisher.publish(event);

        return order.getId();
    }
}
