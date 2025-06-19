package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Tìm sản phẩm theo tên brand
    List<Product> findByBrandIgnoreCase(String brand);

    // Tìm sản phẩm theo khoảng giá
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Tìm sản phẩm theo size
    List<Product> findBySizeIgnoreCase(String size);

    // Tìm sản phẩm theo màu sắc
    List<Product> findByColorIgnoreCase(String color);

    // Tìm sản phẩm có trong kho (stock > 0)
    List<Product> findByStockGreaterThan(Integer stock);

    // Tìm kiếm sản phẩm theo tên (không phân biệt hoa thường)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);
}