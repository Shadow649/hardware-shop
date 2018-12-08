package com.shadow649.hardwareshop.query.controller;

import com.shadow649.hardwareshop.query.api.CreateOrderRequest;
import com.shadow649.hardwareshop.query.api.OrderDetailsDTO;
import com.shadow649.hardwareshop.query.api.OrderStatusDTO;
import com.shadow649.hardwareshop.command.ConfirmOrderCommand;
import com.shadow649.hardwareshop.command.CreateOrderCommand;
import com.shadow649.hardwareshop.dto.DTO;
import com.shadow649.hardwareshop.query.domain.OrderEntry;
import com.shadow649.hardwareshop.query.service.OrderEntryService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Emanuele Lombardi
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CommandGateway commandGateway;
    private final OrderEntryService orderEntryService;

    @ExceptionHandler(AggregateNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badArgument(AggregateNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(java.lang.IllegalStateException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public String badArgument(java.lang.IllegalStateException exception) {
        return exception.getMessage();
    }

    @Autowired
    public OrderController(CommandGateway commandGateway, OrderEntryService orderEntryService) {
        this.commandGateway = commandGateway;
        this.orderEntryService = orderEntryService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public RedirectView createOrder(@DTO(CreateOrderRequest.class) CreateOrderCommand command) {
            String id = commandGateway.sendAndWait(command);
            RedirectView redirectView = new RedirectView();
            redirectView.setUrl(id);
            return redirectView;
    }

    @RequestMapping(value = "{id}/confirm", method = RequestMethod.PUT)
    public ResponseEntity<Void> confirmOrder(@PathVariable String id) throws ExecutionException, InterruptedException {
        commandGateway.sendAndWait(new ConfirmOrderCommand(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @DTO(OrderStatusDTO.class)
    public OrderEntry getStatusById(@PathVariable String id) {
        return orderEntryService.getById(id);
    }

    @RequestMapping(value = "{id}/details", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @DTO(OrderDetailsDTO.class)
    public OrderEntry getDetailsById(@PathVariable String id) {
        return orderEntryService.getById(id);
    }


    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @DTO(OrderDetailsDTO.class)
    public Iterable<OrderEntry> getAllDetails() {
        return orderEntryService.listAllOrders();
    }

    @RequestMapping(value = "/filter", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @DTO(OrderDetailsDTO.class)
    public List<OrderEntry> filterByDate(@RequestParam(name = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date confirmationDateStart,
                                         @RequestParam(name = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date confirmationDateEnd) {
        return orderEntryService.filterByConfirmDate(confirmationDateStart, confirmationDateEnd);
    }
}
