package com.shadow649.hardwareshop.command;

import com.shadow649.hardwareshop.domain.CustomerInfo;
import com.shadow649.hardwareshop.domain.OrderRow;
import com.shadow649.hardwareshop.domain.OrderStatus;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.List;

/**
 * Command to {@link OrderStatus#APPROVED} an order
 *
 * @author Emanuele Lombardi
 */
@Value
public class ApproveOrderCommand {
    @TargetAggregateIdentifier
    private final String id;
    private final CustomerInfo customerInfo;
    private final List<OrderRow> productsDetail;

}
