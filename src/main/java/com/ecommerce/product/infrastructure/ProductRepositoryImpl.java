package com.ecommerce.product.infrastructure;

import com.ecommerce.product.domain.Money;
import com.ecommerce.product.domain.Product;
import com.ecommerce.product.domain.ProductId;
import com.ecommerce.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaRepository jpaRepository;

    @Override
    public void save(Product product) {
        ProductEntity entity = new ProductEntity(
                product.getId().getValue(),
                product.getName(),
                product.getPrice().getAmount(),
                product.getPrice().getCurrency()
        );
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return jpaRepository.findById(id.getValue())
                .map(entity -> new Product(
                        new ProductId(entity.getId()),
                        entity.getName(),
                        new Money(entity.getPrice(), entity.getCurrency())
                ));
    }
}
