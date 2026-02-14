package com.ecommerce.customer.application;

import com.ecommerce.customer.domain.Customer;
import com.ecommerce.customer.domain.CustomerId;
import com.ecommerce.customer.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;

    public Optional<Customer> getCustomer(CustomerId id) {
        return repository.findById(id);
    }
}
