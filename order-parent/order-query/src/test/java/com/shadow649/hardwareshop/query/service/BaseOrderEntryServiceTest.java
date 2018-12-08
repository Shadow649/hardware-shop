package com.shadow649.hardwareshop.query.service;

import com.shadow649.hardwareshop.query.OrderNotFoundException;
import com.shadow649.hardwareshop.query.reposiroty.SpringOrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class BaseOrderEntryServiceTest {

    @MockBean
    private SpringOrderRepository springOrderRepository;

    private BaseOrderEntryService entryService;

    @Before
    public void setUp() {
        entryService = new BaseOrderEntryService(springOrderRepository);
    }

    @Test(expected = OrderNotFoundException.class)
    public void givenTheOrderNotExists_whenGetProductById_ThrowError() {
        entryService.getById("id");
    }
}