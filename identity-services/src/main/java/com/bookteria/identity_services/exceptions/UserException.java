package com.bookteria.identity_services.exceptions;

public class UserException {
    public static class UserNotFoundException extends NotFoundException {
        public UserNotFoundException(String id) {
            super("User with id " + id + " not found");
        }
    }

    public static class UserWithUsernameNotFoundException extends NotFoundException {
        public UserWithUsernameNotFoundException(String username) {
            super("User with username '" + username + "' not found");
        }
    }

    public static class UserWithUserNameAlreadyExists extends BadRequestException {
        public UserWithUserNameAlreadyExists(String username) {
            super("User with username '" + username + "' already exists");
        }
    }
}
