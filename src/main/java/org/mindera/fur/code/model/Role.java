package org.mindera.fur.code.model;


public enum Role {
    MASTER,
    MANAGER,
    ADMIN,
    USER;

    private String role;

    Role() {
    }

    public String getRole() {
        return role;
    }

}
