package com.izi.ecommerce.service;

import com.izi.ecommerce.common.errors.ResourceNotFoundException;
import com.izi.ecommerce.entity.Category;
import com.izi.ecommerce.entity.Product;
import com.izi.ecommerce.entity.ProductCategory;
import com.izi.ecommerce.model.CategoryResponse;
import com.izi.ecommerce.model.PaginatedProductResponse;
import com.izi.ecommerce.model.ProductRequest;
import com.izi.ecommerce.model.ProductResponse;
import com.izi.ecommerce.repository.CategoryRepository;
import com.izi.ecommerce.repository.ProductCategoryRepository;
import com.izi.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;



    @Override
    public Page<ProductResponse> findByPage(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> ProductResponse.fromProductAndCategories(
                        product,
                        getCategoryResponses(product.getProductId())
                ));
    }

    @Override
    public Page<ProductResponse> findByNameAndPageable(String name, Pageable pageable){
        Pageable nativePageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        return productRepository.findByNameAndPageable(name, nativePageable)
            .map(product -> ProductResponse.fromProductAndCategories(
                    product,
                    getCategoryResponses(product.getProductId())
            ));
        }

    @Override
    public Page<ProductResponse> findByCategoryName(String categoryName, Pageable pageable) {
        Pageable nativePageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        return productRepository.findByCategoryName(categoryName, nativePageable)
                .map(product -> ProductResponse.fromProductAndCategories(
                        product,
                        getCategoryResponses(product.getProductId())
                ));
    }

    @Override
    public PaginatedProductResponse convertProductPage(Page<ProductResponse> productPage) {
        return PaginatedProductResponse.builder()
                .data(productPage.getContent())
                .pageNo(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();
    }


    @Override
    public ProductResponse findById(Long productId) {
        Product product = findProductById(productId);

        return ProductResponse.fromProductAndCategories(
                product,
                getCategoryResponses(product.getProductId())
        );
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .stockQuantity(request.getStockQuantity())
                .weight(request.getWeight())
                .build();

        Product savedProduct = productRepository.save(product);
        saveProductCategories(savedProduct.getProductId(), request.getCategoryIds());

        return ProductResponse.fromProductAndCategories(
                savedProduct,
                getCategoryResponses(savedProduct.getProductId())
        );
    }

    @Override
    @Transactional
    public ProductResponse update(Long productId, ProductRequest request) {
        Product product = findProductById(productId);

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setStockQuantity(request.getStockQuantity());
        product.setWeight(request.getWeight());
        Product updatedProduct = productRepository.save(product);
        productCategoryRepository.deleteByIdProductId(productId);
        saveProductCategories(productId, request.getCategoryIds());

        return ProductResponse.fromProductAndCategories(
                updatedProduct,
                getCategoryResponses(updatedProduct.getProductId())
        );
    }

    @Override
    @Transactional
    public void delete(Long productId) {
        Product product = findProductById(productId);

        productCategoryRepository.deleteByIdProductId(productId);
        productRepository.delete(product);
    }



    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produk tidak ditemukan"));
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

    private void saveProductCategories(Long productId, List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }

        List<ProductCategory> productCategories = categoryIds.stream()
                .map(categoryId -> ProductCategory.builder()
                        .id(ProductCategory.ProductCategoryId.builder()
                                .productId(productId)
                                .categoryId(categoryId)
                                .build())
                        .build())
                .toList();

        productCategoryRepository.saveAll(productCategories);
    }

}
