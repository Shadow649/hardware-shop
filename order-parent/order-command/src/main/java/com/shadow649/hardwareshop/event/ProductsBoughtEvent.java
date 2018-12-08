package com.shadow649.hardwareshop.event;

import lombok.Value;

import java.util.List;

@Value
public class ProductsBoughtEvent {
    private final List<String> products;
}
