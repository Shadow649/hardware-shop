package com.shadow649.hardwareshop.query;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Emanuele Lombardi
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException() {
        super();
    }

    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotFoundException(Throwable cause) {
        super(cause);
    }
}
