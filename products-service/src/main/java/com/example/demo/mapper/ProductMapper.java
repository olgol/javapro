package com.example.demo.mapper;

import com.example.demo.dto.ProductResponse;
import com.example.demo.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getAccountNumber(),
                product.getBalance(),
                product.getProductType().name(),
                product.getUser().getId()
        );
    }
}
