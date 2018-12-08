package com.shadow649.hardwareshop.service;


import com.shadow649.hardwareshop.domain.Product;
import com.shadow649.hardwareshop.domain.ProductID;
import com.shadow649.hardwareshop.event.ProductUpdatedEvent;
import com.shadow649.hardwareshop.repository.ProductRepository;
import org.axonframework.common.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Emanuele Lombardi
 */
@Service
public class BaseProductService implements ProductService {

    private final ProductRepository productRepository;
    private final NotificationService notificationService;


    @Autowired
    public BaseProductService(ProductRepository productRepository, NotificationService notificationService) {
        this.productRepository = productRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Iterable<Product> listAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(new ProductID(id)).orElseThrow(() -> new ProductNotFoundException(String.format("Product with id %s not found", id)));
    }


    @Override
    public Product saveProduct(Product product) {
        assertNotExists(product.getId());
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(String id, Product newProduct) {
        if(productRepository.existsById(new ProductID(id))) {
            Product storedProduct = getProductById(id);
            storedProduct.setName(newProduct.getName());
            storedProduct.setPrice(newProduct.getPrice());
            Product updated = productRepository.save(storedProduct);
            notificationService.notify(new ProductUpdatedEvent(storedProduct.getId().id, newProduct.getName(), newProduct.getPrice()));
            return updated;
        }else {
            throw new ProductNotFoundException(String.format("Product with id %s not found", id));
        }

    }

    @Override
    public void deleteProduct(String id) {
        ProductID productID = new ProductID(id);
        if(productRepository.existsById(productID)) {
            productRepository.deleteById(productID);
        } else {
            throw new ProductNotFoundException(String.format("Product with id %s not found", id));
        }
    }

    private void assertNotExists(ProductID id) {
        Assert.isTrue(productRepository.findById(id).orElse(null) == null, () -> "Product already exists");
    }
}
