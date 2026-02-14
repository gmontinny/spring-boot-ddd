package com.ecommerce.web;

import com.ecommerce.customer.application.CreateCustomerUseCase;
import com.ecommerce.customer.domain.CustomerId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Endpoints para gestão de clientes (Bounded Context: Customer)")
public class CustomerController {
    private final CreateCustomerUseCase createCustomerUseCase;

    @PostMapping
    @Operation(summary = "Criar um novo cliente", description = "Cria um cliente e retorna o ID gerado.")
    public ResponseEntity<CustomerId> create(@RequestBody CreateCustomerUseCase.Input input) {
        CustomerId id = createCustomerUseCase.execute(input);
        return ResponseEntity.ok(id);
    }
}
