package com.shadow649.hardwareshop.query.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shadow649.hardwareshop.query.api.CreateOrderRequest;
import com.shadow649.hardwareshop.command.ConfirmOrderCommand;
import com.shadow649.hardwareshop.command.CreateOrderCommand;
import com.shadow649.hardwareshop.domain.OrderStatus;
import com.shadow649.hardwareshop.query.domain.OrderEntry;
import com.shadow649.hardwareshop.query.service.OrderEntryService;
import com.shadow649.hardwareshop.query.domain.OrderLine;
import com.shadow649.hardwareshop.query.OrderNotFoundException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {
    private static final String ORDER_URL = "/orders";

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");


    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private OrderEntryService orderService;

    @MockBean
    private CommandGateway commandGateway;


    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    @Test
    public void createOrder() throws Exception {
        when(commandGateway.sendAndWait(any(CreateOrderCommand.class))).thenReturn("id");

        CreateOrderRequest createOrderRequest =
                new CreateOrderRequest("name", "asd@asd.asd", Collections.singletonList("productID"));

        mockMvc.perform(MockMvcRequestBuilders.post(ORDER_URL)
                .accept(MediaType.APPLICATION_JSON).content(toJson(createOrderRequest)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is3xxRedirection());


    }

    @Test
    public void givenNoOrder_whenConfirmOrder_shouldReturnError() throws Exception {
        when(commandGateway.sendAndWait(any(ConfirmOrderCommand.class))).thenThrow(new AggregateNotFoundException("11", ""));

        mockMvc.perform(MockMvcRequestBuilders.put(ORDER_URL + "/id/confirm" ))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenAnOrderInCreatedState_whenConfirmOrder_shouldReturn() throws Exception {
        when(commandGateway.sendAndWait(any(ConfirmOrderCommand.class))).thenReturn("id");

        mockMvc.perform(MockMvcRequestBuilders.put(ORDER_URL + "/id/confirm" ))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void givenAnOrder_whenGetStatusById_returnTheStatus() throws Exception {
        OrderEntry orderEntry = new OrderEntry(UUID.randomUUID().toString(), "asd@asd.asd", new Date(1), OrderStatus.CREATED);
        OrderLine orderLine = new OrderLine(orderEntry, "123", "test", 123);
        orderEntry.setOrderLines(Collections.singletonList(orderLine));
        when(orderService.getById(orderEntry.getId())).thenReturn(orderEntry);

        mockMvc.perform(MockMvcRequestBuilders.get(ORDER_URL + "/" + orderEntry.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("orderStatus", is("CREATED")));
    }

    @Test
    public void givenNoOrder_whenGetStatusById_returnError() throws Exception {
        when(orderService.getById("id")).thenThrow(new OrderNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get(ORDER_URL + "/id" ))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenNoOrder_whenList_shouldReturnStatus200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ORDER_URL))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void givenAnOrder_whenList_shouldReturnTheOrder() throws Exception {
        OrderEntry orderEntry = new OrderEntry(UUID.randomUUID().toString(), "asd@asd.asd", new Date(1), OrderStatus.CREATED);
        OrderLine orderLine = new OrderLine(orderEntry, "123", "test", 123);
        orderEntry.setOrderLines(Collections.singletonList(orderLine));
        when(orderService.listAllOrders()).thenReturn(Collections.singletonList(orderEntry));

        mockMvc.perform(MockMvcRequestBuilders.get(ORDER_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].customerEmail", is("asd@asd.asd")));
    }

    @Test
    public void givenAnOrderInRage_whenFilter_shouldReturnTheOrder() throws Exception {
        OrderEntry orderEntry = new OrderEntry(UUID.randomUUID().toString(), "asd@asd.asd", new Date(1), OrderStatus.CREATED);
        OrderLine orderLine = new OrderLine(orderEntry, "123", "test", 123);
        orderEntry.setOrderLines(Collections.singletonList(orderLine));
        when(orderService.filterByConfirmDate(any(Date.class), any(Date.class))).thenReturn(Collections.singletonList(orderEntry));

        mockMvc.perform(MockMvcRequestBuilders.get(ORDER_URL + "/filter?start=2017-08-04&end=2019-08-04"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].customerEmail", is("asd@asd.asd")));
    }

    private String toJson(Object request) throws JsonProcessingException {
        return mapper.writeValueAsString(request);
    }

}