package com.shadow649.hardwareshop.service;


import com.shadow649.hardwareshop.domain.Product;

/**
 * Service used to manage products CRUD operation
 * @author Emanuele Lombardi
 */
public interface ProductService {
    Iterable<Product> listAllProducts();

    Product getProductById(String id);

    Product saveProduct(Product product);

    Product updateProduct(String id, Product newProduct);

    void deleteProduct(String id);
}
