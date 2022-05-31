package com.github.phoswald.minecraft.webapp.registration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {

    private String email;
    private String name;
    private String userid;
    private String school;
    private String comment;
}
