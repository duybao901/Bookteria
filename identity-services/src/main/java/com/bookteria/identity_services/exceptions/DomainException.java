package com.bookteria.identity_services.exceptions;

public abstract class DomainException extends RuntimeException {
    private final String title;

    protected DomainException(String title, String message) {
        super(message);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}