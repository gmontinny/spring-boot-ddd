package com.ecommerce.web;

import com.ecommerce.product.application.ProductService;
import com.ecommerce.product.domain.Money;
import com.ecommerce.product.domain.Product;
import com.ecommerce.product.domain.ProductId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Endpoints para gestão de produtos (Bounded Context: Product)")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Criar um novo produto", description = "Cria um produto no catálogo.")
    public ResponseEntity<ProductId> create(@RequestBody CreateProductRequest request) {
        var id = ProductId.generate();
        var product = new Product(
                id,
                request.name(),
                new Money(request.price(), request.currency())
        );
        productService.saveProduct(product);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter produto por ID", description = "Retorna os detalhes de um produto específico.")
    public ResponseEntity<Product> get(@PathVariable UUID id) {
        return productService.getProduct(new ProductId(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public record CreateProductRequest(String name, BigDecimal price, String currency) {}
}
