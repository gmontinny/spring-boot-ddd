package com.ecommerce.customer.infrastructure;

import com.ecommerce.customer.domain.Customer;
import com.ecommerce.customer.domain.CustomerId;
import com.ecommerce.customer.domain.CustomerRepository;
import com.ecommerce.customer.domain.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {
    private final CustomerJpaRepository jpaRepository;

    @Override
    public void save(Customer customer) {
        CustomerEntity entity = new CustomerEntity(
                customer.getId().getValue(),
                customer.getName(),
                customer.getEmail().getAddress()
        );
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Customer> findById(CustomerId id) {
        return jpaRepository.findById(id.getValue())
                .map(entity -> new Customer(
                        new CustomerId(entity.getId()),
                        entity.getName(),
                        new Email(entity.getEmail())
                ));
    }
}
