package com.izi.ecommerce.repository;

import com.izi.ecommerce.entity.ProductCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, ProductCategory.ProductCategoryId> {

    @Query("SELECT productCategory FROM ProductCategory productCategory WHERE productCategory.id.productId = :productId")
    List<ProductCategory> findByProductId(@Param("productId") Long productId);
}
