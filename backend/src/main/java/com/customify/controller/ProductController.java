package com.customify.controller;

import com.customify.dto.product.ProductResponse;
import com.customify.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllMyProducts(Principal principal) {
        return ResponseEntity.ok(productService.getAllProductsByUsername(principal.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(productService.getProductById(id, principal.getName()));
    }

    // Endpoint Multipart para recibir texto + archivo
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestParam("name") String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("basePrice") BigDecimal basePrice,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Principal principal) {

        ProductResponse response = productService.createProduct(
                principal.getName(), name, description, basePrice, file);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, Principal principal) {
        productService.deleteProduct(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/options")
    public ResponseEntity<List<com.customify.dto.option.ProductOptionResponse>> getProductOptions(
            @PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(productService.getProductOptions(id, principal.getName()));
    }

    @PostMapping("/{id}/options")
    public ResponseEntity<com.customify.dto.option.ProductOptionResponse> addProductOption(
            @PathVariable Long id,
            @RequestBody com.customify.dto.option.ProductOptionRequest request,
            Principal principal) {
        return new ResponseEntity<>(productService.addOptionToProduct(id, principal.getName(), request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody com.customify.dto.product.ProductUpdateRequest request,
            Principal principal) {
        return ResponseEntity.ok(productService.updateProduct(id, principal.getName(), request.getName(), request.getDescription(), request.getBasePrice()));
    }
}