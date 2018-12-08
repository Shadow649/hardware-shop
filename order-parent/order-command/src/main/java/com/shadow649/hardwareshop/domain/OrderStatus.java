package com.shadow649.hardwareshop.domain;

public enum OrderStatus {

    /**
     * The order has been initialized. The products order are not yet validated.
     */
    CREATED,

    /**
     * The order has been validated, all products are valid to be purchased.
     * In this example a product is valid to be purchased if exists.
     * Any changes to a product in this order will update the order line
     */
    APPROVED,

    /**
     * The order has been validated, at least one product is not valid to be purchased.
     * In this example a product is not valid if it do not exists.
     */
    REJECTED,

    /**
     * The order has been confirmed, all products are purchased.
     * Any changes to a product in this order will NOT update the order line
     */
    CONFIRMED
}
