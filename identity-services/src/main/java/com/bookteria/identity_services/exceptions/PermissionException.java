package com.bookteria.identity_services.exceptions;

public class PermissionException {
    public static class PermissionNotFoundException extends NotFoundException {
        public PermissionNotFoundException(String permissionName) {
            super("Permission with name " + permissionName + " not found");
        }
    }
}
