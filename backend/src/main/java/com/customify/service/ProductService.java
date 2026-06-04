package com.customify.service;

import com.customify.dto.product.ProductResponse;
import com.customify.exception.ResourceNotFoundException;
import com.customify.model.Product;
import com.customify.model.User;
import com.customify.repository.ProductRepository;
import com.customify.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProductsByUsername(String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return productRepository.findByOwner(owner).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse createProduct(String username, String name, String description, BigDecimal basePrice, MultipartFile file) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        String modelPath = null;
        if (file != null && !file.isEmpty()) {
            modelPath = fileStorageService.storeFile(file);
        }

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setBasePrice(basePrice);
        product.setOwner(owner);
        product.setModelPath(modelPath);

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id, String username) {
        Product product = getProductEntity(id, username);
        return mapToResponse(product);
    }

    @Transactional
    public void deleteProduct(Long id, String username) {
        Product product = getProductEntity(id, username);
        // Aquí podríamos añadir lógica para borrar el archivo físico usando FileStorageService
        productRepository.delete(product);
    }

    // Método auxiliar para comprobar propiedad del producto
    private Product getProductEntity(Long id, String username) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (!product.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("No tienes permiso para acceder a este producto");
        }
        return product;
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .basePrice(product.getBasePrice())
                .modelPath(product.getModelPath())
                .active(product.isActive())
                .build();
    }
}