package com.example.payments.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull Long userId,
        @NotNull Long productId,
        @NotNull @DecimalMin("0.01") BigDecimal amount
) {
}
