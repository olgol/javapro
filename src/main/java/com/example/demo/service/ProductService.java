package com.example.demo.service;

import com.example.demo.dto.ProductResponse;
import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found: id=" + userId);
        }
        return productRepository.findAllByUserIdWithUser(userId).stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findByProductId(Long productId) {
        Product product = productRepository.findByIdWithUser(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: id=" + productId));
        return productMapper.toResponse(product);
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }
}
