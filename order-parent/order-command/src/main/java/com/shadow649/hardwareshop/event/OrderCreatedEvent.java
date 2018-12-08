package com.shadow649.hardwareshop.event;

import com.shadow649.hardwareshop.domain.CustomerInfo;
import com.shadow649.hardwareshop.domain.OrderStatus;
import lombok.Value;

import java.util.List;
/**
 * Marks an order as {@link OrderStatus#CREATED}
 *
 * @author Emanuele Lombardi
 */
@Value
public class OrderCreatedEvent {
    private final String orderID;
    private final CustomerInfo customerInfo;
    private final List<String> products;
}
