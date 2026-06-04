package com.customify.dto.option;

import com.customify.model.OptionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @Builder
public class ProductOptionResponse {
    private Long id;
    private String name;
    private OptionType type;
    private String defaultValue;
    private BigDecimal priceSupplement;
    private List<String> choices;
}