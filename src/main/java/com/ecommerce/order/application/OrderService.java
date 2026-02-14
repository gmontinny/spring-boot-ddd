package com.ecommerce.order.application;

import com.ecommerce.order.application.dto.OrderResponse;
import com.ecommerce.order.domain.Order;
import com.ecommerce.order.domain.OrderId;
import com.ecommerce.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;

    public Optional<OrderResponse> getOrder(OrderId id) {
        return repository.findById(id).map(this::mapToResponse);
    }

    private OrderResponse mapToResponse(Order order) {
        var items = order.getItems().stream()
                .map(item -> new OrderResponse.OrderItemResponse(
                        item.getProductId().getValue(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId().getValue(),
                order.getCustomerId().getValue(),
                order.getStatus().name(),
                items
        );
    }
}
