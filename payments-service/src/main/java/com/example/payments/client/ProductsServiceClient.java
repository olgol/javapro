package com.example.payments.client;

import com.example.payments.dto.ProductDebitRequest;
import com.example.payments.dto.ProductDto;
import com.example.payments.exception.UpstreamServiceException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;

@Component
public class ProductsServiceClient {
    private static final ParameterizedTypeReference<List<ProductDto>> PRODUCT_LIST_TYPE = new ParameterizedTypeReference<>() {
    };

    private final RestClient restClient;

    public ProductsServiceClient(RestClient productsRestClient) {
        this.restClient = productsRestClient;
    }

    public List<ProductDto> getProductsByUserId(Long userId) {
        try {
            return restClient.get()
                    .uri("/api/v1/users/{userId}/products", userId)
                    .retrieve()
                    .body(PRODUCT_LIST_TYPE);
        } catch (RestClientResponseException ex) {
            throw new UpstreamServiceException(ex.getStatusCode(), ex.getResponseBodyAsString());
        }
    }

    public ProductDto getProductById(Long productId) {
        try {
            return restClient.get()
                    .uri("/api/v1/products/{productId}", productId)
                    .retrieve()
                    .body(ProductDto.class);
        } catch (RestClientResponseException ex) {
            throw new UpstreamServiceException(ex.getStatusCode(), ex.getResponseBodyAsString());
        }
    }

    public ProductDto debitProduct(Long productId, ProductDebitRequest request) {
        try {
            return restClient.post()
                    .uri("/api/v1/products/{productId}/debit", productId)
                    .body(request)
                    .retrieve()
                    .body(ProductDto.class);
        } catch (RestClientResponseException ex) {
            HttpStatusCode statusCode = ex.getStatusCode();
            throw new UpstreamServiceException(statusCode, ex.getResponseBodyAsString());
        }
    }
}
