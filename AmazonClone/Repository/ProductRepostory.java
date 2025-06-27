package com.example.AmazonClone.Repository;

import com.example.AmazonClone.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepostory extends JpaRepository<Product, Integer> {

    @Query("SELECT p from Product p WHERE "+
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :key , '%')) OR "+
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :key , '%')) OR "+
            "LOWER(p.brand) LIKE LOWER(CONCAT('%', :key , '%')) OR "+
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :key , '%'))"
    )
    List<Product> searchProducts(String key);
}
