package com.shadow649.hardwareshop.domain;

import com.shadow649.hardwareshop.command.*;
import com.shadow649.hardwareshop.event.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.common.Assert;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.List;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

/**
 * @author Emanuele Lombardi
 */
@Aggregate
public class Order {

    @AggregateIdentifier
    private String id;

    private OrderStatus orderStatus;

    private List<String> products;

    public Order() {
    }

    @CommandHandler
    public Order(CreateOrderCommand command) {
        Assert.isTrue(command.getProducts().size() > 0, () -> "Must be at least one product");
        apply(new OrderCreatedEvent(command.getOrderID(), command.getCustomerInfo(), command.getProducts()));
    }

    @CommandHandler
    public void approve(ApproveOrderCommand command) {
        checkStatus(OrderStatus.CREATED);
        apply(new OrderApprovedEvent(id, command.getCustomerInfo(), command.getProductsDetail()));
    }

    @CommandHandler
    public void reject(RejectOrderCommand command) {
        checkStatus(OrderStatus.CREATED);
        apply(new OrderRejectedEvent(id));
    }

    @CommandHandler
    public void confirm(ConfirmOrderCommand command) {
        checkStatus(OrderStatus.APPROVED);
        apply(new OrderConfirmedEvent(id))
                //bought products could be used to build a projection to get the most attractive product
                .andThenApply(() -> new ProductsBoughtEvent(products));
    }

    @CommandHandler
    public void updateProduct(UpdateOrderProductCommand command) {
        if(products.contains(command.getProductId()) && orderStatus == OrderStatus.APPROVED) {
            apply(new OrderProductUpdatedEvent(id, command.getProductId(), command.getName(), command.getPrice()));
        } else {
            apply(new SkippedProducUpdate(id ,command.getProductId()));
        }
    }


    @EventSourcingHandler
    public void on(OrderCreatedEvent event) {
        this.id = event.getOrderID();
        this.products = event.getProducts();
        this.orderStatus = OrderStatus.CREATED;
    }

    @EventSourcingHandler
    public void on(OrderConfirmedEvent event) {
        this.orderStatus = OrderStatus.CONFIRMED;
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent event) {
        this.orderStatus = OrderStatus.APPROVED;
    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent event) {
        this.orderStatus = OrderStatus.REJECTED;
    }

    private void checkStatus(OrderStatus expected) {
        checkStatus(expected, String.format("Order must be in %s state.", expected));
    }

    private void checkStatus(OrderStatus expected, String errorMessage) {
        Assert.state(expected == orderStatus, () -> errorMessage);
    }
}
