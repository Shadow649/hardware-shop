package com.shadow649.hardwareshop.command;

import com.shadow649.hardwareshop.domain.OrderStatus;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
/**
 * Command to {@link OrderStatus#REJECTED} an order
 *
 * @author Emanuele Lombardi
 */
@Value
public class RejectOrderCommand  {
    @TargetAggregateIdentifier
    private final String id;
}
