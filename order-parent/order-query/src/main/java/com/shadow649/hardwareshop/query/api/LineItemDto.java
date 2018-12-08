package com.shadow649.hardwareshop.query.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonInclude.*;
import static com.shadow649.hardwareshop.id.GenericId.ID_PATTERN;

/**
 * @author Emanuele Lombardi
 */
@Data
public class LineItemDto {

    @NotEmpty
    @Pattern(regexp = ID_PATTERN)
    private final String productId;

    @NotNull
    private final String name;

    @Min(1)
    private final long price;

    @JsonInclude(Include.NON_NULL)
    private String oldName;

    @JsonInclude(Include.NON_DEFAULT)
    private long oldPrice;

    public LineItemDto(@NotEmpty @Pattern(regexp = ID_PATTERN) String productId, @NotNull String name, @Min(1) long price) {
        this(productId, name, price, null, 0);
    }

    public LineItemDto(@NotEmpty @Pattern(regexp = ID_PATTERN) String productId, @NotNull String name, @Min(1) long price, String oldName, long oldPrice) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.oldName = oldName;
        this.oldPrice = oldPrice;
    }
}
