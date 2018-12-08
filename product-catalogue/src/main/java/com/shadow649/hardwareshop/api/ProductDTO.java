package com.shadow649.hardwareshop.api;

import com.shadow649.hardwareshop.dto.BaseDTO;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Emanuele Lombardi
 */
@Value
public class ProductDTO implements BaseDTO {

    @NotNull
    private String name;

    @Min(1)
    private long price;
}
