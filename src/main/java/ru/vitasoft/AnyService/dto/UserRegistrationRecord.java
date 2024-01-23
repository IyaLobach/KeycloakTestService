package ru.vitasoft.AnyService.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationRecord {

    private String username;
    private String email;
    private String lastName;
    private String firstName;
    private String password;

}
