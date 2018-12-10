package com.shadow649.hardwareshop.query.eventhandler;

import com.shadow649.hardwareshop.domain.OrderRow;
import com.shadow649.hardwareshop.domain.OrderStatus;
import com.shadow649.hardwareshop.event.*;
import com.shadow649.hardwareshop.query.domain.OrderEntry;
import com.shadow649.hardwareshop.query.domain.OrderLine;
import com.shadow649.hardwareshop.query.service.OrderEntryService;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Listen to {@link com.shadow649.hardwareshop.domain.Order} events and create projections
 * @author Emanuele Lombardi
 */
@Component
public class OrderProjectionEventHandler {

    private OrderEntryService service;

    @Autowired
    public OrderProjectionEventHandler(OrderEntryService service) {
        this.service = service;
    }

    @EventHandler
    public void on(OrderApprovedEvent event) {
        OrderEntry orderEntry = new OrderEntry(event.getId(), event.getCustomerInfo().getEmail(), new Date(), OrderStatus.APPROVED);
        List<OrderLine> orderLines = convertToQueryLines(event.getProductsDetail(), orderEntry);
        orderEntry.setOrderLines(orderLines);
        service.save(orderEntry);
    }

    @EventHandler
    public void on(OrderCreatedEvent event) {
        OrderEntry orderEntry = new OrderEntry(event.getOrderID(), event.getCustomerInfo().getEmail(), new Date(), OrderStatus.CREATED);
        service.save(orderEntry);
    }

    @EventHandler
    public void on(OrderRejectedEvent event) {
        OrderEntry entry = service.getById(event.getId());
        entry.setStatus(OrderStatus.REJECTED);
        service.save(entry);
    }

    @EventHandler
    public void on(OrderConfirmedEvent event) {
        OrderEntry entry = service.getById(event.getId());
        entry.setConfirmedDate(new Date());
        entry.setStatus(OrderStatus.CONFIRMED);
        service.save(entry);
    }

    @EventHandler
    public void on(OrderProductUpdatedEvent event) {
        OrderEntry orderEntry = service.getById(event.getOrderId());
        List<OrderLine> orderLines = orderEntry.getOrderLines();
        boolean updated = false;
        for (OrderLine orderLine : orderLines) {
            if(orderLine.getProductId().equals(event.getProductId())) {
                orderLine.setOldPrice(orderLine.getPrice());
                orderLine.setOldName(orderLine.getName());
                orderLine.setPrice(event.getPrice().longValue());
                orderLine.setName(event.getName());
                orderLine.setUpdated(true);
                updated = true;
            }
        }
        if(updated) {
            service.save(orderEntry);
        }
    }

    private List<OrderLine> convertToQueryLines(List<OrderRow> productsDetail, OrderEntry orderEntry) {
        List<OrderLine> result = new ArrayList<>();
        for (OrderRow line : productsDetail) {
            OrderLine orderLine = new OrderLine(orderEntry, line.getProductID().getId(), line.getDescription(), line.getPrice().longValue());
            result.add(orderLine);

        }
        return result;
    }
}
