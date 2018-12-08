package com.shadow649.hardwareshop.event;

import lombok.Value;

import java.math.BigDecimal;

/**
 * Event fired to notify a change into the product catalog
 * @author Emanuele Lombardi
 */
@Value
public class ProductUpdatedEvent {
    private final String productId;
    private final String name;
    private final BigDecimal price;
}
