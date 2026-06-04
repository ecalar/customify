package com.customify.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter @Setter @Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private String modelPath;
    private BigDecimal basePrice;
    private boolean active;
}