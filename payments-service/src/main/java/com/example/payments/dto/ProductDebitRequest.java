package com.example.payments.dto;

import java.math.BigDecimal;

public record ProductDebitRequest(BigDecimal amount) {
}
