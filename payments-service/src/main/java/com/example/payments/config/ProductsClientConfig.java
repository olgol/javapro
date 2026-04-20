package com.example.payments.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ProductsClientConfig {

    @Bean
    RestClient productsRestClient(
            RestClient.Builder builder,
            @Value("${products.service.base-url}") String productsBaseUrl
    ) {
        return builder.baseUrl(productsBaseUrl).build();
    }
}
