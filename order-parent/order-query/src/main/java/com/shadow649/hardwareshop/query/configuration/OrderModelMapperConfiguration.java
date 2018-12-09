package com.shadow649.hardwareshop.query.configuration;

import com.shadow649.hardwareshop.query.api.LineItemDto;
import com.shadow649.hardwareshop.query.api.OrderDetailsDTO;
import com.shadow649.hardwareshop.query.api.OrderStatusDTO;
import com.shadow649.hardwareshop.query.domain.OrderEntry;
import com.shadow649.hardwareshop.query.domain.OrderLine;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emanuele Lombardi
 */
@Configuration
public class OrderModelMapperConfiguration {

    @Autowired
    public OrderModelMapperConfiguration(ModelMapper modelMapper) {
        Converter<OrderEntry, OrderDetailsDTO> createOrderRequestCreateOrderCommandConverter = new Converter<OrderEntry, OrderDetailsDTO>() {
            @Override
            public OrderDetailsDTO convert(MappingContext<OrderEntry, OrderDetailsDTO> context) {
                OrderEntry source = context.getSource();
                List<OrderLine> orderLines = source.getOrderLines();
                List<LineItemDto> lines = orderLines.stream().map(orderLine -> new LineItemDto(orderLine.getProductId(), orderLine.getName(), orderLine.getPrice(), orderLine.getOldName(), orderLine.getOldPrice())).collect(Collectors.toList());
                long totalPrice = lines.stream().map(LineItemDto :: getPrice).reduce(0L, (price1, price2) -> price1 + price2);
                return new OrderDetailsDTO(source.getId(), source.getCustomerEmail(), lines, source.getStatus(), source.getConfirmedDate(), totalPrice);
            }
        };

        Converter<OrderEntry, OrderStatusDTO> orderEntryOrderStatusDTOConverter = new Converter<OrderEntry, OrderStatusDTO>() {
            @Override
            public OrderStatusDTO convert(MappingContext<OrderEntry, OrderStatusDTO> context) {
                OrderEntry source = context.getSource();
                long totalPrice = source.getOrderLines().stream().map(OrderLine::getPrice).reduce(0L, (price1, price2) -> price1 + price2);
                return new OrderStatusDTO(source.getStatus(), totalPrice);
            }
        };
        modelMapper.addConverter(createOrderRequestCreateOrderCommandConverter);
        modelMapper.addConverter(orderEntryOrderStatusDTOConverter);
    }

}
