package com.shadow649.hardwareshop.query.configuration;

import com.shadow649.hardwareshop.query.api.ConfirmOrderRequest;
import com.shadow649.hardwareshop.query.api.CreateOrderRequest;
import com.shadow649.hardwareshop.command.ConfirmOrderCommand;
import com.shadow649.hardwareshop.command.CreateOrderCommand;
import com.shadow649.hardwareshop.domain.CustomerInfo;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

/**
 *
 * @author Emanuele Lombardi
 */
@Configuration
public class CommandModelMapperConfiguration {

    @Autowired
    public CommandModelMapperConfiguration(ModelMapper modelMapper) {
        Converter<CreateOrderRequest, CreateOrderCommand> createOrderRequestCreateOrderCommandConverter = new Converter<CreateOrderRequest, CreateOrderCommand>() {
            @Override
            public CreateOrderCommand convert(MappingContext<CreateOrderRequest, CreateOrderCommand> context) {
                CreateOrderRequest source = context.getSource();
                return new CreateOrderCommand(UUID.randomUUID().toString(), new CustomerInfo(source.getCustomerName(), source.getCustomerEmail()), source.getProductIDs());
            }
        };
        modelMapper.addConverter(createOrderRequestCreateOrderCommandConverter);
        Converter<ConfirmOrderRequest, ConfirmOrderCommand> confirmOrderRequestConfirmOrderCommandConverter = new Converter<ConfirmOrderRequest, ConfirmOrderCommand>() {
            @Override
            public ConfirmOrderCommand convert(MappingContext<ConfirmOrderRequest, ConfirmOrderCommand> context) {
                ConfirmOrderRequest source = context.getSource();
                return new ConfirmOrderCommand(source.getOrderId());
            }
        };
        modelMapper.addConverter(confirmOrderRequestConfirmOrderCommandConverter);
    }

}
