package ru.vitasoft.AnyService.services;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ru.vitasoft.AnyService.dto.UserRegistrationRecord;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserService implements KeyCloakUserI {

    private final Keycloak keycloak;
    private final String realm = "PersonalCabinetTest";


    @Override
    public UserRegistrationRecord createUser(String username, String email, String lastName, String firstName, String password) {
        // создание нового пользователя
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setEmail(email);
        userRepresentation.setEmailVerified(true);
        userRepresentation.setFirstName(firstName);
        userRepresentation.setLastName(lastName);

        // учетные данные
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(password);
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);


        // установка учетных данных пользователя
        userRepresentation.setCredentials(List.of(credentialRepresentation));

        //
        RealmResource realm1 = keycloak.realm(this.realm);
        UsersResource usersResource = realm1.users();

        // сохранение нового пользователя
        Response response = usersResource.create(userRepresentation);

        if (Objects.equals(201, response.getStatus())) {
            return UserRegistrationRecord.builder()
                    .email(email)
                    .firstName(firstName)
                    .lastName(lastName)
                    .password(password)
                    .username(username)
                    .build();
        }

        return null;
    }

    public UsersResource getUsersResource() {
        RealmResource realm1 = keycloak.realm(this.realm);
        return realm1.users();
    }


    @Override
    public UserRepresentation getUserById(String userId) {
        return getUsersResource().get(userId).toRepresentation();
    }

    @Override
    public void deleteUserById(String userId) {
        getUsersResource().delete(userId);
    }
}
