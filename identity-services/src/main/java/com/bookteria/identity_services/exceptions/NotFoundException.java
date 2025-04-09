package com.bookteria.identity_services.exceptions;

public class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super("Not Found", message);
    }
}
