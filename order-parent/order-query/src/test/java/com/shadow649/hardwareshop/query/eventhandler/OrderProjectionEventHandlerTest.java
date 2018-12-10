package com.shadow649.hardwareshop.query.eventhandler;

import com.shadow649.hardwareshop.domain.CustomerInfo;
import com.shadow649.hardwareshop.domain.OrderRow;
import com.shadow649.hardwareshop.domain.OrderStatus;
import com.shadow649.hardwareshop.domain.ProductID;
import com.shadow649.hardwareshop.event.OrderApprovedEvent;
import com.shadow649.hardwareshop.event.OrderConfirmedEvent;
import com.shadow649.hardwareshop.event.OrderProductUpdatedEvent;
import com.shadow649.hardwareshop.query.OrderNotFoundException;
import com.shadow649.hardwareshop.query.domain.OrderEntry;
import com.shadow649.hardwareshop.query.domain.OrderLine;
import com.shadow649.hardwareshop.query.service.OrderEntryService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OrderProjectionEventHandlerTest {

    @Mock
    private OrderEntryService service;

    @Captor
    private ArgumentCaptor<OrderEntry> entryCaptor;

    private OrderProjectionEventHandler orderProjectionEventHandler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        orderProjectionEventHandler = new OrderProjectionEventHandler(service);
    }

    @Test(expected = OrderNotFoundException.class)
    public void givenOrderNotExists_whenServiceGetById_ThrowOrderNotFoundException() {
        when(service.getById(anyString())).thenThrow(new OrderNotFoundException());

        orderProjectionEventHandler.on(new OrderConfirmedEvent("id"));
    }

    @Test
    public void whenAnOrderIsApproved_thenSaveProjection() {
        CustomerInfo customerInfo = new CustomerInfo("Mario", "mario@rossi.it");
        List<OrderRow> rows = Collections.singletonList(new OrderRow(new ProductID("pID"), "product1", new BigDecimal(11)));
        orderProjectionEventHandler.on(new OrderApprovedEvent("id", customerInfo, rows));

        verify(service).save(entryCaptor.capture());

        OrderEntry saved = entryCaptor.getValue();
        assertThat(saved.getOrderLines().size()).isEqualTo(rows.size());
        assertThat(saved.getOrderLines().get(0).getName()).isEqualTo("product1");
    }

    @Test
    public void whenAProductInOrderIsUpdated_thenTheUpdatedEntryIsSaved() {
        OrderEntry orderEntry = new OrderEntry("test1", "asd@asd.asd", new Date(1), OrderStatus.CONFIRMED);
        OrderLine orderLine = new OrderLine(orderEntry, "123", "test", 123);
        orderEntry.setOrderLines(Collections.singletonList(orderLine));
        when(service.getById("test1")).thenReturn(orderEntry);

        orderProjectionEventHandler.on(new OrderProductUpdatedEvent("test1", "123", "newName", new BigDecimal(1000)));

        verify(service).save(entryCaptor.capture());

        OrderEntry saved = entryCaptor.getValue();
        assertThat(saved.getOrderLines().get(0).getName()).isEqualTo("newName");
        assertThat(saved.getOrderLines().get(0).getOldName()).isEqualTo("test");
        assertThat(saved.getOrderLines().get(0).isUpdated()).isTrue();
    }

    @Test
    public void whenAProductNotInTheOrderIsUpdated_thenNoUpdate() {
        OrderEntry orderEntry = new OrderEntry("test1", "asd@asd.asd", new Date(1), OrderStatus.CONFIRMED);
        OrderLine orderLine = new OrderLine(orderEntry, "123", "test", 123);
        orderEntry.setOrderLines(Collections.singletonList(orderLine));
        when(service.getById("test1")).thenReturn(orderEntry);

        orderProjectionEventHandler.on(new OrderProductUpdatedEvent("test1", "5", "newName", new BigDecimal(1000)));

        verify(service).getById("test1");
        verifyNoMoreInteractions(service);

    }
}