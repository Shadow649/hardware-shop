package com.shadow649.hardwareshop.query.eventhandler;

import com.shadow649.hardwareshop.command.UpdateOrderProductCommand;
import com.shadow649.hardwareshop.event.ProductUpdatedEvent;
import com.shadow649.hardwareshop.query.domain.OrderEntry;
import com.shadow649.hardwareshop.query.reposiroty.SpringOrderRepository;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

/**
 * Handles the product update workflow
 * @author Emanuele Lombardi
 */
@Component
public class UpdateOrderProductsEventHandler {

    private final CommandBus commandBus;
    private final SpringOrderRepository repository;

    public UpdateOrderProductsEventHandler(CommandBus commandBus, SpringOrderRepository repository) {
        this.commandBus = commandBus;
        this.repository = repository;
    }

    @EventHandler
    public void on(ProductUpdatedEvent event) {
        List<OrderEntry> all = repository.findAll();
        for (OrderEntry orderEntry : all) {
            UpdateOrderProductCommand command = new UpdateOrderProductCommand(orderEntry.getId(), event.getProductId(), event.getName(), event.getPrice());
            commandBus.dispatch(asCommandMessage(command), LoggingCallback.INSTANCE);
        }
    }

}
