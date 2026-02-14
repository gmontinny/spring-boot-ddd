package com.ecommerce.product.application;

import com.ecommerce.product.domain.Product;
import com.ecommerce.product.domain.ProductId;
import com.ecommerce.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public Optional<Product> getProduct(ProductId id) {
        return repository.findById(id);
    }

    public void saveProduct(Product product) {
        repository.save(product);
    }
}
