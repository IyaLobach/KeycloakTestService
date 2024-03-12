package ru.vitasoft.back.services.interfaces;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import ru.vitasoft.back.back.dto.UserRegistrationRecord;

import java.util.List;

public interface KeyCloakUserI {

    UserRegistrationRecord createUser(UserRegistrationRecord user);
    UserRepresentation getUserById(String userId);
    void deleteUserById(String userId);
    UserResource getUserResource(String userId);
    void forgotPassword(String username);
    List<UserRepresentation> getUserByUsername(String username);

}
