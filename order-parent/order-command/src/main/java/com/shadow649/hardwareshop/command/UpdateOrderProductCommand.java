package com.shadow649.hardwareshop.command;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
/**
 * Starts the order-update-product workflow
 *
 * @author Emanuele Lombardi
 */
@Value
public class UpdateOrderProductCommand {
    @TargetAggregateIdentifier
    private String orderID;

    private final String productId;
    private final String name;
    private final BigDecimal price;

}

