package com.shadow649.hardwareshop.event;

import lombok.Value;

/**
 * Notify that a product in the order does not exists
 *
 * @author Emanuele Lombardi
 */
@Value
public class SkippedProducUpdate {
    private final String orderId;
    private final String productId;

}
