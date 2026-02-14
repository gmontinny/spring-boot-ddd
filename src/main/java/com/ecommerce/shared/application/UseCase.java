package com.ecommerce.shared.application;

public interface UseCase<IN, OUT> {
    OUT execute(IN input);
}
