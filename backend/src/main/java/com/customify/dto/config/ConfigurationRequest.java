package com.customify.dto.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class ConfigurationRequest {
    @NotNull
    private Long productId;

    @NotNull
    private String data; // El JSON con las opciones elegidas por el cliente

    @NotNull
    private BigDecimal totalPrice;
}