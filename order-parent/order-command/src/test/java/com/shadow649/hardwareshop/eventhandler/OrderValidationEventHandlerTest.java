package com.shadow649.hardwareshop.eventhandler;

import com.shadow649.hardwareshop.command.ApproveOrderCommand;
import com.shadow649.hardwareshop.command.RejectOrderCommand;
import com.shadow649.hardwareshop.domain.OrderRow;
import com.shadow649.hardwareshop.domain.ProductID;
import com.shadow649.hardwareshop.event.OrderCreatedEvent;
import com.shadow649.hardwareshop.service.ProductCatalogueService;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderValidationEventHandlerTest {

    @Mock
    private ProductCatalogueService productCatalogueService;

    @Mock
    private CommandBus commandBus;

    @Captor
    ArgumentCaptor<CommandMessage<ApproveOrderCommand>> approveCommandCaptor;

    @Captor
    ArgumentCaptor<CommandMessage<RejectOrderCommand>> rejectCommandCaptor;

    private OrderValidationEventHandler orderValidationEventHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        orderValidationEventHandler = new OrderValidationEventHandler(productCatalogueService, commandBus);
    }

    @Test
    public void givenProductsExist_whenValidateOrder_thenApproveOrder() {
        List<String> products = Collections.singletonList("productId");
        List<OrderRow> expectedRow = Collections.singletonList(
                new OrderRow(ProductID.randomId(), "description", new BigDecimal(1)));
        when(productCatalogueService.productsExist(new HashSet<>(products))).thenReturn(true);
        when(productCatalogueService.getOrderRows(new HashSet<>(products)))
                .thenReturn(expectedRow);

        orderValidationEventHandler.on(new OrderCreatedEvent("orderId", null, products));

        verify(productCatalogueService).getOrderRows(new HashSet<>(products));

        verify(commandBus).dispatch(approveCommandCaptor.capture(), any());

        assertThat(approveCommandCaptor.getValue().getPayload().getProductsDetail()).isEqualTo(expectedRow);
    }

    @Test
    public void givenProductsNotExist_whenValidateOrder_thenApproveOrder() {
        List<String> products = Collections.singletonList("productId");
        when(productCatalogueService.productsExist(new HashSet<>(products))).thenReturn(false);

        orderValidationEventHandler.on(new OrderCreatedEvent("orderId", null, products));

        verify(commandBus).dispatch(rejectCommandCaptor.capture(), any());

        assertThat(rejectCommandCaptor.getValue().getPayload()).isNotNull();
    }
}