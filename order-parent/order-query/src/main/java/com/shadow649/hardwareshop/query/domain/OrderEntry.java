package com.shadow649.hardwareshop.query.domain;

import com.shadow649.hardwareshop.domain.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * An order projection that present the order to the user
 * @author Emanuele Lombardi
 */
@Entity
@Data
public class OrderEntry {

    @Id
    private final String id;

    @NotNull
    private final String customerEmail;

    @NotNull
    private final Date createdDate;

    @NotNull
    private OrderStatus status;

    private Date confirmedDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private List<OrderLine> orderLines;

    public OrderEntry(String id, @NotNull String customerEmail, @NotNull Date createdDate, @NotNull OrderStatus status) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.createdDate = createdDate;
        this.status = status;
    }
}
