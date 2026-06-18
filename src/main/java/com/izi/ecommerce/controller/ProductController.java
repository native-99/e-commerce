package com.izi.ecommerce.controller;

import com.izi.ecommerce.model.ProductRequest;
import com.izi.ecommerce.model.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    // GET localhost:3000/products/2
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(
            @PathVariable(value = "id") Long productId
    ) {
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name("product " + productId)
                        .price(BigDecimal.ONE)
                        .description("deskripsi produk")
                        .build()
        );
    }

    // GET localhost:3000/products
    @GetMapping("/")
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        return ResponseEntity.ok(
                List.of(
                        ProductResponse.builder()
                                .name("product 1")
                                .price(BigDecimal.ONE)
                                .description("deskripsi produk")
                                .build(),

                        ProductResponse.builder()
                                .name("product 2")
                                .price(BigDecimal.ONE)
                                .description("deskripsi produk")
                                .build()
                )
        );
    }

    // POST localhost:3000/products
    @PostMapping("/")
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest request
    ) {
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name(request.getName())
                        .price(request.getPrice())
                        .description(request.getDescription())
                        .build()
        );
    }

    // PUT localhost:3000/products/2
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @RequestBody ProductRequest request,
            @PathVariable(name = "id") Long productId
    ) {
        return ResponseEntity.ok(
                ProductResponse.builder()
                        .name(request.getName() + " " + productId)
                        .price(request.getPrice())
                        .description(request.getDescription())
                        .build()
        );
    }
}