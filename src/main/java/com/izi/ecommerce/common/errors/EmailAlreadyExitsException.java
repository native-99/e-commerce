package com.izi.ecommerce.common.errors;

public class EmailAlreadyExitsException extends RuntimeException {
    public EmailAlreadyExitsException(String message) {
        super(message);
    }
}
