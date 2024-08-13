package org.mindera.fur.code.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The role of a person as a master", example = "Master", required = true)
public enum Role {
    /**
     * The role of a person as a master
     */
    @Schema(description = "The role of a person as a master", example = "Master", required = true)
    MASTER("Master"),

    @Schema(description = "The role of a person as a manager", example = "Manager", required = true)
    MANAGER("Manager"),

    @Schema(description = "The role of a person as an admin", example = "Admin", required = true)
    ADMIN("Admin"),

    @Schema(description = "The role of a person as a user", example = "User", required = true)
    USER("User");


    private String role;

    Role(String role) {
    }


    /**
     * Converts a string representation of a role to its corresponding Role enum.
     *
     * @param role The string representation of the role.
     * @return The corresponding Role enum.
     * @throws IllegalArgumentException if no matching Role enum is found for the provided string.
     */
    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.role.equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No enum constant with role " + role);
    }

    /**
     * Gets the string representation of the role.
     *
     * @return The string representation of the role.
     */
    public String getRole() {
        return role;
    }

}
