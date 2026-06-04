package com.customify.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product_option")
@Getter @Setter @NoArgsConstructor
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptionType type;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "price_supplement")
    private BigDecimal priceSupplement = BigDecimal.ZERO;

    @ElementCollection
    @CollectionTable(name = "option_choices", joinColumns = @JoinColumn(name = "option_id"))
    @Column(name = "choice")
    private List<String> choices;
}