package com.shadow649.hardwareshop.eventhandler;

import com.shadow649.hardwareshop.command.ApproveOrderCommand;
import com.shadow649.hardwareshop.command.RejectOrderCommand;
import com.shadow649.hardwareshop.domain.OrderRow;
import com.shadow649.hardwareshop.event.OrderCreatedEvent;
import com.shadow649.hardwareshop.service.ProductCatalogueService;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

@Component
public class OrderValidationEventHandler {

    private final ProductCatalogueService productCatalogueService;
    private final CommandBus commandBus;

    @Autowired
    public OrderValidationEventHandler(ProductCatalogueService productCatalogueService, CommandBus commandBus) {
        this.productCatalogueService = productCatalogueService;
        this.commandBus = commandBus;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        Set<String> productsId = new HashSet<>(event.getProducts());
        if(productCatalogueService.productsExist(productsId)) {
            List<OrderRow> productsDetail = productCatalogueService.getOrderRows(productsId);
            ApproveOrderCommand approve = new ApproveOrderCommand(event.getOrderID(), event.getCustomerInfo(), productsDetail);
            commandBus.dispatch(asCommandMessage(approve), LoggingCallback.INSTANCE);
        } else {
            RejectOrderCommand reject = new RejectOrderCommand(event.getOrderID());
            commandBus.dispatch(asCommandMessage(reject), LoggingCallback.INSTANCE);
        }
    }



}
