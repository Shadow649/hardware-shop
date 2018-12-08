package com.shadow649.hardwareshop.event;

import com.shadow649.hardwareshop.domain.OrderStatus;
import lombok.Value;
/**
 * Marks an order as {@link OrderStatus#REJECTED}
 *
 * @author Emanuele Lombardi
 */
@Value
public class OrderRejectedEvent {
    private final String id;

}
