package com.example.demo.dto;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductType;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String accountNumber,
        BigDecimal balance,
        ProductType productType,
        Long userId
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getAccountNumber(),
                product.getBalance(),
                product.getProductType(),
                product.getUser().getId()
        );
    }
}
