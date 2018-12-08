package com.shadow649.hardwareshop.query.api;


import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import static com.shadow649.hardwareshop.id.GenericId.ID_PATTERN;

/**
 * Used to mark an order to be confirmed.
 *
 * @author Emanuele Lombardi
 */
@Value
public class ConfirmOrderRequest {

    @NotEmpty
    @Pattern(regexp = ID_PATTERN)
    private String orderId;
}
