package com.shadow649.hardwareshop.controller;

import com.shadow649.hardwareshop.api.ProductDTO;
import com.shadow649.hardwareshop.api.ProductViewDTO;
import com.shadow649.hardwareshop.domain.Product;
import com.shadow649.hardwareshop.dto.DTO;
import com.shadow649.hardwareshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Emanuele Lombardi
 */
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String badArgument(IllegalArgumentException exception) {
        return exception.getMessage();
    }

    @RequestMapping(method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @DTO(ProductViewDTO.class)
    public Iterable<Product> list(){
        return productService.listAllProducts();
    }

    @RequestMapping(value = "{id}", method= RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product getById(@PathVariable String id){
        return productService.getProductById(id);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @DTO(ProductViewDTO.class)
    public Product saveProduct(@DTO(ProductDTO.class) @Valid Product product){
        return productService.saveProduct(product);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateProduct(@PathVariable String id, @DTO(ProductDTO.class) @Valid  Product product){
        productService.updateProduct(id, product);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




}
