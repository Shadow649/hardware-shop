package com.shadow649.hardwareshop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Contains all the product relevant information
 * @author Emanuele Lombardi
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @EmbeddedId
    private ProductID id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;
}
