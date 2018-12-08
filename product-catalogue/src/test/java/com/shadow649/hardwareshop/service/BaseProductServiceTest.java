package com.shadow649.hardwareshop.service;

import com.shadow649.hardwareshop.domain.Product;
import com.shadow649.hardwareshop.domain.ProductID;
import com.shadow649.hardwareshop.event.ProductUpdatedEvent;
import com.shadow649.hardwareshop.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration
public class BaseProductServiceTest {

    @MockBean
    private ProductRepository repository;

    @MockBean
    NotificationService notificationService;

    @Captor
    ArgumentCaptor<ProductUpdatedEvent> eventCaptor;

    private BaseProductService productService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        productService = new BaseProductService(repository, notificationService);
    }

    @Test
    public void listAllProducts() {
        productService.listAllProducts();
        verify(repository).findAll();
    }

    @Test
    public void givenTheProductExists_whenGetProductById_invokeRepository() {
        Product mockedProduct = getMockedProduct();

        when(repository.findById(mockedProduct.getId())).thenReturn(Optional.of(mockedProduct));

        productService.getProductById(mockedProduct.getId().id);

        verify(repository).findById(mockedProduct.getId());
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenTheProductNotExists_whenGetProductById_ThrowError() {
        productService.getProductById(ProductID.randomId().id);
    }

    @Test
    public void givenTheProductNotExists_whenAdd_invokeRepository() {
        Product mockedProduct = getMockedProduct();

        productService.saveProduct(mockedProduct);

        verify(repository).findById(mockedProduct.getId());
        verify(repository).save(mockedProduct);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenTheProductExists_whenAdd_ThrowError() {
        Product mockedProduct = getMockedProduct();
        when(repository.findById(mockedProduct.getId())).thenReturn(Optional.of(mockedProduct));

        productService.saveProduct(mockedProduct);

    }

    @Test
    public void givenTheProductExists_whenUpdate_NotifyAndUpdate() {
        Product mockedProduct = getMockedProduct();
        when(repository.existsById(mockedProduct.getId())).thenReturn(true);
        when(repository.findById(mockedProduct.getId())).thenReturn(Optional.of(mockedProduct));

        productService.updateProduct(mockedProduct.getId().id, mockedProduct);

        verify(notificationService).notify(eventCaptor.capture());
        verify(mockedProduct).setName(any());
        verify(mockedProduct).setPrice(any());
        assertThat(eventCaptor.getValue().getProductId()).isEqualTo(mockedProduct.getId().id);
        verify(repository).save(mockedProduct);
    }

    @Test
    public void givenTheProduct_whenDelete_invokeRepository() {
        ProductID productID = ProductID.randomId();
        when(repository.existsById(productID)).thenReturn(true);

        productService.deleteProduct(productID.id);

        verify(repository).deleteById(productID);
    }

    @Test(expected = ProductNotFoundException.class)
    public void givenNoProduct_whenDelete_ThrowError() {
        productService.deleteProduct(ProductID.randomId().id);

    }

    private Product getMockedProduct() {
        ProductID productID = ProductID.randomId();
        Product mockedProduct = mock(Product.class);
        when(mockedProduct.getId()).thenReturn(productID);
        return mockedProduct;
    }

}