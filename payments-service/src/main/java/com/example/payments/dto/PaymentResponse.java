package com.example.payments.dto;

import java.math.BigDecimal;

public record PaymentResponse(
        String status,
        Long userId,
        Long productId,
        BigDecimal amount,
        BigDecimal balanceAfter
) {
}
