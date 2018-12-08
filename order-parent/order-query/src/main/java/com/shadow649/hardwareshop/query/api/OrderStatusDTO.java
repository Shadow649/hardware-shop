package com.shadow649.hardwareshop.query.api;

import com.shadow649.hardwareshop.domain.OrderStatus;
import lombok.Value;

@Value
public class OrderStatusDTO {

    private final OrderStatus orderStatus;
    private final long totalPrice;
}
