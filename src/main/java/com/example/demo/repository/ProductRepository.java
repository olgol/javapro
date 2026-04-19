package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN FETCH p.user WHERE p.user.id = :userId ORDER BY p.id")
    List<Product> findAllByUserIdWithUser(@Param("userId") Long userId);

    @Query("SELECT p FROM Product p JOIN FETCH p.user WHERE p.id = :productId")
    Optional<Product> findByIdWithUser(@Param("productId") Long productId);
}
