package com.shadow649.hardwareshop.domain;

import com.shadow649.hardwareshop.command.*;
import com.shadow649.hardwareshop.event.*;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class OrderCommandHandlerTest {

    private FixtureConfiguration<Order> testFixture;

    @Before
    public void setUp() throws Exception {
        testFixture = new AggregateTestFixture<>(Order.class);

        testFixture.registerCommandDispatchInterceptor(new BeanValidationInterceptor<>());
    }

    @Test
    public void createOrder() {
        String orderId = UUID.randomUUID().toString();
        CustomerInfo testInfo = new CustomerInfo("test", "test@test.test");
        List<String> products = Collections.singletonList("productId");

        testFixture.givenNoPriorActivity()
                .when(new CreateOrderCommand(orderId,testInfo, products))
                .expectEvents(new OrderCreatedEvent(orderId, testInfo, products));
    }

    @Test
    public void createOrder_whitZeroProduct() {
        String orderId = UUID.randomUUID().toString();
        CustomerInfo testInfo = new CustomerInfo("test", "test@test.test");

        testFixture.givenNoPriorActivity()
                .when(new CreateOrderCommand(orderId,testInfo, Collections.emptyList()))
                .expectException(IllegalArgumentException.class);
    }

    @Test
    public void approveOrder() {
        String orderId = UUID.randomUUID().toString();
        CustomerInfo testInfo = new CustomerInfo("test", "test@test.test");
        List<String> products = Collections.singletonList("productId");
        List<OrderRow> row = Collections.singletonList(
                new OrderRow(ProductID.randomId(), "description", new BigDecimal(1)));

        testFixture.given(new OrderCreatedEvent(orderId, testInfo, products))
                .when(new ApproveOrderCommand(orderId, testInfo, row))
                .expectEvents(new OrderApprovedEvent(orderId, testInfo, row));
    }

    @Test
    public void rejectOrder() {
        String orderId = UUID.randomUUID().toString();
        CustomerInfo testInfo = new CustomerInfo("test", "test@test.test");
        List<String> products = Collections.singletonList("productId");

        testFixture.given(new OrderCreatedEvent(orderId, testInfo, products))
                .when(new RejectOrderCommand(orderId))
                .expectEvents(new OrderRejectedEvent(orderId));
    }

    @Test
    public void confirmOrder() {
        String orderId = UUID.randomUUID().toString();
        CustomerInfo testInfo = new CustomerInfo("test", "test@test.test");
        List<String> products = Collections.singletonList("productId");
        List<OrderRow> row = Collections.singletonList(
                new OrderRow(ProductID.randomId(), "description", new BigDecimal(1)));

        testFixture.given(new OrderCreatedEvent(orderId, testInfo, products))
                .andGiven(new OrderApprovedEvent(orderId, testInfo, row))
                .when(new ConfirmOrderCommand(orderId))
                .expectEvents(new OrderConfirmedEvent(orderId), new ProductsBoughtEvent(products));
    }

    @Test
    public void updateProduct_whenOrderApproved() {
        String orderId = UUID.randomUUID().toString();
        CustomerInfo testInfo = new CustomerInfo("test", "test@test.test");
        List<String> products = Collections.singletonList("productId");
        List<OrderRow> row = Collections.singletonList(
                new OrderRow(ProductID.randomId(), "description", new BigDecimal(1)));

        testFixture.given(new OrderCreatedEvent(orderId, testInfo, products))
                .andGiven(new OrderApprovedEvent(orderId, testInfo, row))
                .when(new UpdateOrderProductCommand(orderId, "productId", "test", new BigDecimal(1)))
                .expectEvents(new OrderProductUpdatedEvent(orderId, "productId", "test", new BigDecimal(1)));
    }

    @Test
    public void updateProduct_whenOrderNotApproved() {
        String orderId = UUID.randomUUID().toString();
        CustomerInfo testInfo = new CustomerInfo("test", "test@test.test");
        List<String> products = Collections.singletonList("productId");

        testFixture.given(new OrderCreatedEvent(orderId, testInfo, products))
                .when(new UpdateOrderProductCommand(orderId, "productId", "test", new BigDecimal(1)))
                .expectEvents(new SkippedProducUpdate(orderId, "productId"));
    }
}