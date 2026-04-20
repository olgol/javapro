package com.example.payments.dto;

import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String accountNumber,
        BigDecimal balance,
        String productType,
        Long userId
) {
}
