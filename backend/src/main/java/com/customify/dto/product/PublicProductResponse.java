package com.customify.dto.product;

import com.customify.dto.option.ProductOptionResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @Builder
public class PublicProductResponse {
    private Long id;
    private String name;
    private String description;
    private String modelPath;
    private BigDecimal basePrice;
    private List<ProductOptionResponse> options;
}