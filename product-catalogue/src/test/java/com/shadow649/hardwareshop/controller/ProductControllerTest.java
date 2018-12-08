package com.shadow649.hardwareshop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shadow649.hardwareshop.api.ProductDTO;
import com.shadow649.hardwareshop.domain.Product;
import com.shadow649.hardwareshop.domain.ProductID;
import com.shadow649.hardwareshop.service.ProductNotFoundException;
import com.shadow649.hardwareshop.service.ProductService;
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

import java.math.BigDecimal;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductControllerTest {

    private static final String PRODUCT_URL = "/products";

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");


    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ProductService productService;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
                .build();
    }

    @Test
    public void givenNoProducts_whenList_shouldReturnStatus200() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_URL))
                .andDo(document("get product example"))
                .andExpect(status().isOk());

        verify(productService).listAllProducts();
    }

    @Test
    public void givenAProduct_whenList_shouldReturnTheProduct() throws Exception {
        when(productService.listAllProducts()).thenReturn(
                Collections.singletonList(
                        new Product(ProductID.randomId(), "testProduct", new BigDecimal(1))));
        mockMvc.perform(MockMvcRequestBuilders.get(PRODUCT_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is("testProduct")));
    }

    @Test
    public void givenNoProduct_whenAdd_shouldReturnStatus201() throws Exception {
        ProductDTO toSave = new ProductDTO("testProduct", 1);
        mockMvc.perform(MockMvcRequestBuilders.post(PRODUCT_URL)
                .accept(MediaType.APPLICATION_JSON).content(toJson(toSave)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        verify(productService).saveProduct(any(Product.class));
    }

    @Test
    public void givenAProduct_whenAddTheSameProduct_shouldReturnError() throws Exception {
        ProductDTO toSave = new ProductDTO("testProduct", 1);
        when(productService.saveProduct(any(Product.class))).thenThrow(new IllegalArgumentException());

        mockMvc.perform(MockMvcRequestBuilders.post(PRODUCT_URL)
                .accept(MediaType.APPLICATION_JSON).content(toJson(toSave)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void givenAProduct_whenUpdateTheProduct_shouldReturnStatusOk() throws Exception {
        ProductDTO newProduct = new ProductDTO("testProduct", 1);
        Product mockedProduct = mock(Product.class);
        when(productService.getProductById("1")).thenReturn(mockedProduct);

        mockMvc.perform(MockMvcRequestBuilders.put(PRODUCT_URL + "/1")
                .accept(MediaType.APPLICATION_JSON).content(toJson(newProduct)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(204));
    }

    @Test
    public void givenNoProduct_whenUpdateTheProduct_shouldReturnError() throws Exception {
        when(productService.updateProduct("1", null)).thenThrow(ProductNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.put(PRODUCT_URL + "/1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    private String toJson(ProductDTO mockProduct) throws JsonProcessingException {
        return mapper.writeValueAsString(mockProduct);
    }

}