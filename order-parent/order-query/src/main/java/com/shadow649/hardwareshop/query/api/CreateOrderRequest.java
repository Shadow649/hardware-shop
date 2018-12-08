package com.shadow649.hardwareshop.query.api;


import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Initiate the order creation process
 * @author Emanuele Lombardi
 */
@Value
public class CreateOrderRequest {

    @NotNull
    private String customerName;

    @NotNull
    @Email
    private String customerEmail;

    @NotEmpty
    private List<String> productIDs;
}
