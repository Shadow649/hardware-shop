package com.shadow649.hardwareshop.event;

import com.shadow649.hardwareshop.domain.CustomerInfo;
import com.shadow649.hardwareshop.domain.OrderRow;
import com.shadow649.hardwareshop.domain.OrderStatus;
import lombok.Value;

import java.util.List;

/**
 * Marks an order as {@link OrderStatus#APPROVED}
 *
 * @author Emanuele Lombardi
 */
@Value
public class OrderApprovedEvent {
    private final String id;
    private final CustomerInfo customerInfo;
    private final List<OrderRow> productsDetail;

}
