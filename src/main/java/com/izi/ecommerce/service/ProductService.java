package com.izi.ecommerce.service;

import com.izi.ecommerce.model.PaginatedProductResponse;
import com.izi.ecommerce.model.ProductRequest;
import com.izi.ecommerce.model.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    Page<ProductResponse> findByPage(Pageable pageable);

    ProductResponse findById(Long productId);

    ProductResponse create(ProductRequest request);

    ProductResponse update(Long productId, ProductRequest request);

    void delete(Long productId);

    Page<ProductResponse> findByNameAndPageable(String name, Pageable pageable);

    Page<ProductResponse> findByCategoryName(String categoryName, Pageable pageable);

    PaginatedProductResponse convertProductPage(Page<ProductResponse> productPage);
}
