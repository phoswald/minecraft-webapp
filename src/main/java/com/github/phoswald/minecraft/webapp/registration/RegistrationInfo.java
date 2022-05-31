package com.github.phoswald.minecraft.webapp.registration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationInfo {

    private String id;
    private String timestamp;
    private String email;
    private String name;
    private String userId;
    private String school;
    private String comment;
}
