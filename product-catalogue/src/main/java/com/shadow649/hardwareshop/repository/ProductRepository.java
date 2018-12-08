package com.shadow649.hardwareshop.repository;


import com.shadow649.hardwareshop.domain.Product;
import com.shadow649.hardwareshop.domain.ProductID;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Emanuele Lombardi
 */
public interface ProductRepository extends CrudRepository<Product, ProductID> {
}
