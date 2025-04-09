package com.bookteria.identity_services.exceptions;

public class BadRequestException extends DomainException {
    public BadRequestException(String message) {
        super("Bad Request", message);
    }
}
