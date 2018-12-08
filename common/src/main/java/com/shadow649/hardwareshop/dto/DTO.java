package com.shadow649.hardwareshop.dto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks request/response domain object to be transformed from/to a DTO class
 *
 * @author Emanuele Lombardi
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DTO {
    Class value();
}