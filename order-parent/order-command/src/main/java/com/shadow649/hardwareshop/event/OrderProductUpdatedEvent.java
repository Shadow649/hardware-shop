package com.shadow649.hardwareshop.event;

import lombok.Value;

import java.math.BigDecimal;

/**
 * Notify that a product in the order was updated
 *
 * @author Emanuele Lombardi
 */
@Value
public class OrderProductUpdatedEvent {
    private final String orderId;
    private final String productId;
    private final String name;
    private final BigDecimal price;

}
