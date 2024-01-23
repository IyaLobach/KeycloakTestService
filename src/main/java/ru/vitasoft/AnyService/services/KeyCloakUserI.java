package ru.vitasoft.AnyService.services;

import org.keycloak.representations.idm.UserRepresentation;
import ru.vitasoft.AnyService.dto.UserRegistrationRecord;

public interface KeyCloakUserI {

    UserRegistrationRecord createUser(String username, String email, String lastName, String firstName, String password);
    UserRepresentation getUserById(String userId);
    void deleteUserById(String userId);
}
