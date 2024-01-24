package ru.vitasoft.AnyService.services;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.vitasoft.AnyService.dto.UserRegistrationRecord;
import ru.vitasoft.AnyService.services.interfaces.KeyCloakUserI;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserService implements KeyCloakUserI {

    private final Keycloak keycloak;
    private final String realm = "PersonalCabinetTest";


    @Override
    public UserRegistrationRecord createUser(UserRegistrationRecord user) {
        // создание нового пользователя
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());

        // учетные данные
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(user.getPassword());
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
            List<UserRepresentation> representationList = usersResource.searchByUsername(user.getUsername(), true);
            if (!CollectionUtils.isEmpty(representationList)) {
                UserRepresentation userRepresentation1 = representationList.stream().filter(userRepresentationn -> Objects.equals(false, userRepresentationn.isEmailVerified())).findFirst().orElse(null);
                emailVerification(userRepresentation1.getId());
            }
        }

        return user;
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

    public void emailVerification(String userId) {
        UsersResource usersResource = getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    @Override
    public UserResource getUserResource(String userId) {
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }
}
