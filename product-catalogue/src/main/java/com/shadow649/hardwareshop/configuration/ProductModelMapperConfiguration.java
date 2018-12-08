package com.shadow649.hardwareshop.configuration;

import com.shadow649.hardwareshop.api.ProductDTO;
import com.shadow649.hardwareshop.api.ProductViewDTO;
import com.shadow649.hardwareshop.domain.Product;
import com.shadow649.hardwareshop.domain.ProductID;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * @author Emanuele Lombardi
 */
@Configuration
public class ProductModelMapperConfiguration {

    @Autowired
    public ProductModelMapperConfiguration(ModelMapper modelMapper) {
        Converter<ProductDTO, Product> productDTOProductConverter = new Converter<ProductDTO, Product>() {
            @Override
            public Product convert(MappingContext<ProductDTO, Product> context) {
                ProductDTO source = context.getSource();
                return new Product(ProductID.randomId(), source.getName(), new BigDecimal(source.getPrice()));
            }
        };

        Converter<Product, ProductViewDTO> productProductViewDTOConverter = new Converter<Product, ProductViewDTO>() {
            @Override
            public ProductViewDTO convert(MappingContext<Product, ProductViewDTO> context) {
                Product source = context.getSource();
                return new ProductViewDTO(source.getId().id, source.getName(), source.getPrice().longValue());
            }
        };
        modelMapper.addConverter(productDTOProductConverter);
        modelMapper.addConverter(productProductViewDTOConverter);
    }

}
