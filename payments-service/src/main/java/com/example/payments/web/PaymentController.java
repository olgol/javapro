package com.example.payments.web;

import com.example.payments.dto.PaymentRequest;
import com.example.payments.dto.PaymentResponse;
import com.example.payments.dto.ProductDto;
import com.example.payments.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/users/{userId}/products")
    public List<ProductDto> getProducts(@PathVariable Long userId) {
        return paymentService.getUserProducts(userId);
    }

    @PostMapping("/payments")
    public PaymentResponse executePayment(@Valid @RequestBody PaymentRequest request) {
        return paymentService.executePayment(request);
    }
}
