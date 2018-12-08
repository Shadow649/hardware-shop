package com.shadow649.hardwareshop.api;

import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Emanuele Lombardi
 */
@Value
public class ProductViewDTO {
    @NotEmpty
    private final String id;

    @NotNull
    private final String name;

    @Min(1)
    private final long price;
}
