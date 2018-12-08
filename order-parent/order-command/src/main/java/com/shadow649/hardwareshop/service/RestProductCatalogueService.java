package com.shadow649.hardwareshop.service;

import com.shadow649.hardwareshop.domain.OrderRow;
import com.shadow649.hardwareshop.domain.Product;
import com.shadow649.hardwareshop.client.ProductCatalogClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestProductCatalogueService implements ProductCatalogueService {

    private final ProductCatalogClient catalogClient;

    @Autowired
    public RestProductCatalogueService(@Qualifier("Internal") ProductCatalogClient catalogClient) {
        this.catalogClient = catalogClient;
    }

    @Override
    public boolean productsExist(Set<String> productsId) {
        return catalogClient.getCatalog().keySet().containsAll(productsId);
    }

    @Override
    public List<OrderRow> getOrderRows(Set<String> productsId) {
        return productsId.stream().map(productId -> {
            Product product = catalogClient.getProductById(productId);
            return new OrderRow(product.getId(), product.getName(), product.getPrice());
        }).collect(Collectors.toList());
    }
}
