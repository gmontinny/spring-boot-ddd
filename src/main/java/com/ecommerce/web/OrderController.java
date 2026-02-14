package com.ecommerce.web;

import com.ecommerce.order.application.CreateOrderUseCase;
import com.ecommerce.order.application.OrderService;
import com.ecommerce.order.application.dto.CreateOrderRequest;
import com.ecommerce.order.application.dto.OrderResponse;
import com.ecommerce.order.domain.OrderId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Endpoints para gestão de pedidos (Bounded Context: Order)")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Realizar um novo pedido", description = "Cria um pedido validando cliente e estoque de produtos.")
    public ResponseEntity<OrderId> create(@RequestBody CreateOrderRequest request) {
        OrderId id = createOrderUseCase.execute(request);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes de um pedido", description = "Retorna os dados completos de um pedido.")
    public ResponseEntity<OrderResponse> get(@PathVariable UUID id) {
        return orderService.getOrder(new OrderId(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
