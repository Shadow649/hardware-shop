package com.shadow649.hardwareshop.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shadow649.hardwareshop.id.GenericId;

import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class ProductID extends GenericId {

    public ProductID() {
        super(UUID.randomUUID().toString());
    }

    public ProductID(@JsonProperty("id") String id) {
        super(id);
    }

    public static ProductID randomId() {
        return new ProductID();
    }
}
