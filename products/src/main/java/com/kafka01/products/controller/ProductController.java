package com.kafka01.products.controller;

import com.kafka01.products.dto.CreateProductRequest;
import com.kafka01.products.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kafka01.common.dto.BaseResponse;
/**
 * 상품 컨트롤러
 */
@Validated
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;

    }
    /**
     * 상품 생성 요청
     * @param request
     * @return
     */
    @PostMapping("/products")
    public BaseResponse<String> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return BaseResponse.body(HttpStatus.CREATED, productService.createProduct(request));
    }
}
