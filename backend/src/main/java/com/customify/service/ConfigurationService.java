package com.customify.service;

import com.customify.dto.config.ConfigurationRequest;
import com.customify.exception.ResourceNotFoundException;
import com.customify.model.Configuration;
import com.customify.model.Product;
import com.customify.repository.ConfigurationRepository;
import com.customify.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long saveConfiguration(ConfigurationRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        Configuration config = new Configuration();
        config.setProduct(product);
        config.setData(request.getData());
        config.setTotalPrice(request.getTotalPrice());

        Configuration savedConfig = configurationRepository.save(config);

        // Devolvemos el ID de la configuración generada para poder mostrar un resumen al cliente
        return savedConfig.getId();
    }
}