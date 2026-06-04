package com.customify.dto.option;

import com.customify.model.OptionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class ProductOptionRequest {
    @NotBlank
    private String name;

    @NotNull
    private OptionType type;

    private String defaultValue;
    private BigDecimal priceSupplement;
    private List<String> choices;
}