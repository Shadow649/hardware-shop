package com.shadow649.hardwareshop.query.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author Emanuele Lombardi
 */
@Entity
@Data
public class OrderLine {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @NotNull
    private OrderEntry entry;

    @NotNull
    private String productId;

    @NotNull
    private String name;

    @NotNull
    private long price;

    private boolean updated;

    private String oldName;

    private long oldPrice;

    public OrderLine(@NotNull OrderEntry entry, @NotNull String productId, @NotNull String name, @NotNull long price) {
        this.entry = entry;
        this.productId = productId;
        this.name = name;
        this.price = price;
    }
}
