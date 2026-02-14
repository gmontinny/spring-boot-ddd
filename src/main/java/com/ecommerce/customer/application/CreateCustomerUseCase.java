package com.ecommerce.customer.application;

import com.ecommerce.customer.domain.Customer;
import com.ecommerce.customer.domain.CustomerId;
import com.ecommerce.customer.domain.CustomerRepository;
import com.ecommerce.customer.domain.Email;
import com.ecommerce.shared.application.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCustomerUseCase implements UseCase<CreateCustomerUseCase.Input, CustomerId> {
    private final CustomerRepository repository;

    @Override
    public CustomerId execute(Input input) {
        Customer customer = new Customer(
                CustomerId.generate(),
                input.name(),
                new Email(input.email())
        );
        repository.save(customer);
        return customer.getId();
    }

    public record Input(String name, String email) {}
}
