package com.shadow649.hardwareshop;

import com.shadow649.hardwareshop.controller.ProductController;
import org.axonframework.eventhandling.EventBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class ApplicationInitializationTest {

    @Autowired
    private ProductController productController;

    @Autowired
    private EventBus eventBus;

    @Test
    public void contexLoads() throws Exception {
        assertThat(productController).isNotNull();
        assertThat(eventBus).isNotNull();

    }

}