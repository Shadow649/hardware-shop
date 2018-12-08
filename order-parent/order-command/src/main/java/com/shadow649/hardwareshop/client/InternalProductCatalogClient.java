package com.shadow649.hardwareshop.client;

import com.shadow649.hardwareshop.domain.Product;
import com.shadow649.hardwareshop.domain.ProductID;
import com.shadow649.hardwareshop.repository.ProductRepository;
import com.shadow649.hardwareshop.service.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * It's like a mock implementation. Since the application it's a monolith ATM sounds ok for a demo.
 *
 * @author Emanuele Lombardi
 */
@Qualifier("Internal")
@Service
public class InternalProductCatalogClient implements ProductCatalogClient {

    private final ProductRepository repository;

    @Autowired
    public InternalProductCatalogClient(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Product getProductById(String id) {
        return repository.findById(new ProductID(id)).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public Map<String, Product> getCatalog() {
        Iterable<Product> products = repository.findAll();
        return StreamSupport.stream(products.spliterator(), false).collect(Collectors.toConcurrentMap(product -> product.getId().getId(), product -> product));
    }
}
