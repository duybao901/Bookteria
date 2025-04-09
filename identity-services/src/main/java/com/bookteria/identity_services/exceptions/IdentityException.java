package com.bookteria.identity_services.exceptions;

public class IdentityException {
    public static class PassWordNotMatchException extends BadRequestException {
        public PassWordNotMatchException() {
            super("Password does not match");
        }
    }

    public static class TokenEception extends DomainException {
        public TokenEception(String message) {
            super("Token Exception", message);
        }
    }
}
