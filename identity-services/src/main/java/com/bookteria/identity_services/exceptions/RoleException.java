package com.bookteria.identity_services.exceptions;

public class RoleException {
    public static class RoleNotFoundException extends NotFoundException {
        public RoleNotFoundException(String roleName) {
            super("Role with name " + roleName + " not found");
        }
    }
}
