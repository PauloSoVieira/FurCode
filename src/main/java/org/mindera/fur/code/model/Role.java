package org.mindera.fur.code.model;

public enum Role {
    MASTER("Master"),
    MANAGER("Manager"),
    ADMIN("Admin"),
    USER("User");

    private String role;

    Role(String role) {
    }

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.role.equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No enum constant with role " + role);
    }

    public String getRole() {
        return role;
    }

}
