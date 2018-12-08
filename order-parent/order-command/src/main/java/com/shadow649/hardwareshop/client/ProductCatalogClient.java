package com.shadow649.hardwareshop.client;

import com.shadow649.hardwareshop.domain.Product;
import com.shadow649.hardwareshop.service.ProductNotFoundException;

import java.util.Map;

/**
 * @author Emanuele Lombardi
 */
public interface ProductCatalogClient {
    /**
     * Gets a product information by its ID
     * @param id The product id
     * @return the product information
     * @throws ProductNotFoundException if the product does not exist
     */
    Product getProductById(String id);

    /**
     * Get all the products catalog as a map id -> product
     * @return The products catalog
     */
    Map<String, Product> getCatalog();
}
