package com.example.payments.service;

import com.example.payments.client.ProductsServiceClient;
import com.example.payments.dto.PaymentRequest;
import com.example.payments.dto.PaymentResponse;
import com.example.payments.dto.ProductDebitRequest;
import com.example.payments.dto.ProductDto;
import com.example.payments.exception.PaymentValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final ProductsServiceClient productsServiceClient;

    public List<ProductDto> getUserProducts(Long userId) {
        return productsServiceClient.getProductsByUserId(userId);
    }

    public PaymentResponse executePayment(PaymentRequest request) {
        ProductDto product = productsServiceClient.getProductById(request.productId());

        if (!request.userId().equals(product.userId())) {
            throw new PaymentValidationException("Product id=" + request.productId() + " does not belong to user id=" + request.userId());
        }

        if (product.balance().compareTo(request.amount()) < 0) {
            throw new PaymentValidationException(
                    "Insufficient funds for product id=" + request.productId() + ": balance=" + product.balance()
            );
        }

        ProductDto debited = productsServiceClient.debitProduct(product.id(), new ProductDebitRequest(request.amount()));
        return new PaymentResponse("SUCCESS", request.userId(), request.productId(), request.amount(), debited.balance());
    }
}
