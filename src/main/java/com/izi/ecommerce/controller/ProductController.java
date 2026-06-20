package com.izi.ecommerce.controller;

import com.izi.ecommerce.common.errors.ResourceNotFoundException;
import com.izi.ecommerce.entity.Category;
import com.izi.ecommerce.entity.Product;
import com.izi.ecommerce.entity.ProductCategory;
import com.izi.ecommerce.model.CategoryResponse;
import com.izi.ecommerce.model.ProductRequest;
import com.izi.ecommerce.model.ProductResponse;
import com.izi.ecommerce.repository.CategoryRepository;
import com.izi.ecommerce.repository.ProductCategoryRepository;
import com.izi.ecommerce.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;

    // GET localhost:3000/api/v1/products/2
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(
            @PathVariable(value = "id") Long productId
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produk tidak ditemukan"));

        return ResponseEntity.ok(
                ProductResponse.fromProductAndCategories(
                        product,
                        getCategoryResponses(product.getProductId())
                )
        );
    }

    // GET localhost:3000/api/v1/products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        return ResponseEntity.ok(
                productRepository.findAll()
                        .stream()
                        .map(product -> ProductResponse.fromProductAndCategories(
                                product,
                                getCategoryResponses(product.getProductId())
                        ))
                        .toList()
        );
    }

    // POST localhost:3000/api/v1/products
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request
    ) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .stockQuantity(0)
                .weight(BigDecimal.valueOf(1000))
                .build();

        Product savedProduct = productRepository.save(product);

        return ResponseEntity.ok(
                ProductResponse.fromProductAndCategories(savedProduct, List.of())
        );
    }

    // PUT localhost:3000/api/v1/products/2
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @Valid @RequestBody ProductRequest request,
            @PathVariable(name = "id") Long productId
    ) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produk tidak ditemukan"));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());

        Product updatedProduct = productRepository.save(product);

        return ResponseEntity.ok(
                ProductResponse.fromProductAndCategories(
                        updatedProduct,
                        getCategoryResponses(updatedProduct.getProductId())
                )
        );
    }

    private List<CategoryResponse> getCategoryResponses(Long productId) {
        List<Long> categoryIds = productCategoryRepository.findByProductId(productId)
                .stream()
                .map(ProductCategory::getId)
                .map(ProductCategory.ProductCategoryId::getCategoryId)
                .toList();

        if (categoryIds.isEmpty()) {
            return List.of();
        }

        List<Category> categories = categoryRepository.findAllById(categoryIds);

        return categories.stream()
                .map(CategoryResponse::fromCategory)
                .toList();
    }
}
