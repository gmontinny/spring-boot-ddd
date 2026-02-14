package com.ecommerce.order.infrastructure;

import com.ecommerce.customer.domain.CustomerId;
import com.ecommerce.order.domain.Order;
import com.ecommerce.order.domain.OrderId;
import com.ecommerce.order.domain.OrderItem;
import com.ecommerce.order.domain.OrderRepository;
import com.ecommerce.product.domain.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository jpaRepository;

    @Override
    public void save(Order order) {
        var items = order.getItems().stream()
                .map(item -> new OrderItemEntity(
                        item.getId(),
                        item.getProductId().getValue(),
                        item.getQuantity(),
                        item.getPrice()
                ))
                .collect(Collectors.toList());

        var entity = new OrderEntity(
                order.getId().getValue(),
                order.getCustomerId().getValue(),
                order.getStatus(),
                items
        );

        jpaRepository.save(entity);
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return jpaRepository.findById(id.getValue())
                .map(entity -> {
                    var order = new Order(
                            new OrderId(entity.getId()),
                            new CustomerId(entity.getCustomerId())
                    );
                    // Aqui precisaríamos de um jeito de setar o status e os itens no objeto de domínio.
                    // Para simplificar o DDD sem expor setters, poderíamos usar um construtor ou método de fábrica.
                    // No momento o Order.java está bem simples.
                    entity.getItems().forEach(itemEntity -> 
                        order.addItem(new OrderItem(
                            itemEntity.getId(),
                            new ProductId(itemEntity.getProductId()),
                            itemEntity.getQuantity(),
                            itemEntity.getPrice()
                        ))
                    );
                    return order;
                });
    }
}
