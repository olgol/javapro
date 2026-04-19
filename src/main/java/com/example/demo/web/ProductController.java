package com.example.demo.web;

import com.example.demo.dto.ProductResponse;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/users/{userId}/products")
    public List<ProductResponse> listByUser(@PathVariable Long userId) {
        return productService.findAllByUserId(userId);
    }

    @GetMapping("/products/{productId}")
    public ProductResponse getById(@PathVariable Long productId) {
        return productService.findByProductId(productId);
    }
}
