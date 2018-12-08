package com.shadow649.hardwareshop.query.api;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.shadow649.hardwareshop.domain.OrderStatus;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.*;
import static com.shadow649.hardwareshop.id.GenericId.ID_PATTERN;

/**
 * @author Emanuele Lombardi
 */
@Value
@JsonInclude(Include.NON_NULL)
public class OrderDetailsDTO {

    @NotEmpty
    @Pattern(regexp = ID_PATTERN)
    private String orderID;

    private String customerEmail;

    @NotNull
    @NotEmpty
    private List<LineItemDto> lineItems;

    private OrderStatus status;

    @Min(1)
    private long totalPrice;



}
