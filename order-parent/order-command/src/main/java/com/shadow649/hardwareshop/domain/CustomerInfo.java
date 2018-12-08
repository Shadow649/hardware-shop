package com.shadow649.hardwareshop.domain;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Contains the needed customer information
 *
 * @author Emanuele Lombardi
 */
@Value
public class CustomerInfo {

    @NotNull
    private final String name;

    @Email
    private final String email;
}
