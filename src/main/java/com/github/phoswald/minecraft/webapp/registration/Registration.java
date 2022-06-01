package com.github.phoswald.minecraft.webapp.registration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Registration {

    private String id;
    private String timestamp;
    private String email;
    private String name;
    private String userId;
    private String school;
    private String comment;

    void validate() {
        requiresNonEmpty(email, "email");
        requiresNonEmpty(name, "name");
        requiresNonEmpty(userId, "userId");
    }

    private void requiresNonEmpty(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Field " + name + " must be set.");
        }
    }
}
