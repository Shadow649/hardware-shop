package com.shadow649.hardwareshop.command;

import com.shadow649.hardwareshop.domain.CustomerInfo;
import com.shadow649.hardwareshop.domain.OrderStatus;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.List;
/**
 * Command to {@link OrderStatus#CREATED} an order
 *
 * @author Emanuele Lombardi
 */
@Value
public class CreateOrderCommand {

    @TargetAggregateIdentifier
    private String orderID;

    private CustomerInfo customerInfo;

    private List<String> products;
}
