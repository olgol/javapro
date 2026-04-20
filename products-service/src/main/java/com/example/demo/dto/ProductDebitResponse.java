package com.example.demo.dto;

import java.math.BigDecimal;

public record ProductDebitResponse(
        Long productId,
        BigDecimal debitedAmount,
        BigDecimal balanceAfter
) {
}
