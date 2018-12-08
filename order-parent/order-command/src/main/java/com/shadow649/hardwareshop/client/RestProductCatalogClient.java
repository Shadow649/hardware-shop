package com.shadow649.hardwareshop.client;

import com.shadow649.hardwareshop.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Qualifier("Rest")
public class RestProductCatalogClient implements ProductCatalogClient {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Product getProductById(String id) {
        return restTemplate.getForObject("http://127.0.0.1:8080/product/" + id, Product.class);
    }

    @Override
    public Map<String, Product> getCatalog() {
        ResponseEntity<List<Product>> response = restTemplate.exchange("http://127.0.0.1:8080/product/list",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {
                });
        List<Product> products = response.getBody();
        return products.stream().collect(Collectors.toConcurrentMap(product -> product.getId().getId(), product -> product));
    }
}
