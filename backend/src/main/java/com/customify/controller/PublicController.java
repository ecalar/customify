package com.customify.controller;

import com.customify.dto.config.ConfigurationRequest;
import com.customify.dto.product.PublicProductResponse;
import com.customify.service.ConfigurationService;
import com.customify.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Permitimos peticiones desde cualquier origen para el visor 3D
public class PublicController {

    private final ProductService productService;
    private final ConfigurationService configurationService;

    @GetMapping("/products/{id}")
    public ResponseEntity<PublicProductResponse> getPublicProductDetails(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getPublicProduct(id));
    }

    @PostMapping("/configurations")
    public ResponseEntity<?> saveConfiguration(@Valid @RequestBody ConfigurationRequest request) {
        Long configId = configurationService.saveConfiguration(request);
        return new ResponseEntity<>(Map.of("message", "Configuración guardada con éxito", "configurationId", configId), HttpStatus.CREATED);
    }
}