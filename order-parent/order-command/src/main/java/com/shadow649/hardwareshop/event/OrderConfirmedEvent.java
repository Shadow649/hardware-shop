package com.shadow649.hardwareshop.event;

import com.shadow649.hardwareshop.domain.OrderStatus;
import lombok.Value;
/**
 * Marks an order as {@link OrderStatus#CONFIRMED}
 *
 * @author Emanuele Lombardi
 */
@Value
public class OrderConfirmedEvent {
    private final String id;

}
