package com.shadow649.hardwareshop.command;

import com.shadow649.hardwareshop.domain.OrderStatus;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
/**
 * Command to {@link OrderStatus#CONFIRMED} an order
 *
 * @author Emanuele Lombardi
 */
@Value
public class ConfirmOrderCommand {
    @TargetAggregateIdentifier
    private final String id;
}
