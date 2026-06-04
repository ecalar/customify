package com.customify.repository;

import com.customify.model.Product;
import com.customify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByOwner(User owner);
    List<Product> findByOwnerId(Long ownerId);
}