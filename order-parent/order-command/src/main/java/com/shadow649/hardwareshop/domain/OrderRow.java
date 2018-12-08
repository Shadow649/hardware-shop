package com.shadow649.hardwareshop.domain;

import lombok.Value;

import java.math.BigDecimal;

/**
 * Contains the needed customer information
 *
 * @author Emanuele Lombardi
 */
@Value
public class OrderRow {

    private final ProductID productID;

    private final String description;
    
    private final BigDecimal price;
}
