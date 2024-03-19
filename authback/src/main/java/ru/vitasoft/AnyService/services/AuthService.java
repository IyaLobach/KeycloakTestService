package ru.vitasoft.AnyService.services;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.vitasoft.AnyService.config.JwtProvider;
import ru.vitasoft.AnyService.exception.CustomConflictException;
import ru.vitasoft.AnyService.model.User;
import ru.vitasoft.AnyService.model.dto.UserDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application-authback.properties")
public class AuthService {

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final JwtProvider jwtProvider;
    private final Keycloak keycloak;
    private final static String DEFAULT_ROLE = "ROLE_EMPLOYEE";
    private final static String UPDATE_PASSWORD = "UPDATE_PASSWORD";
    private final static String REDIRECT_URI = "http://localhost:8085/authback/hello";

    public void auth(String username, String password) {
        Keycloak user = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .build();
        jwtProvider.setCookie(user.tokenManager().getAccessTokenString());
    }

    public User getIdentity() {
        JwtAuthenticationToken token = jwtProvider.getTokenFromRequest();
        return User.builder()
                .email((String) token.getTokenAttributes().get("email"))
                .id(token.getName())
                .roles(token.getAuthorities())
                .build();
    }

    public void registration(UserDto user) {


        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setEmailVerified(false);
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
            emailVerification(userRepresentation.getId());
            assertRole(userRepresentation.getId(), DEFAULT_ROLE);
        } else {
            throw new CustomConflictException("Пользователь с таким именем уже существует");
        }
    }

    public void assertRole(String userId, String roleName) {
        UserResource userResource = getUserResource(userId);

        RolesResource rolesResource = getRoleResource();
        RoleRepresentation roleRepresentation = rolesResource.get(roleName).toRepresentation();
        ClientRepresentation service = keycloak.realm(this.realm).clients().findByClientId(clientId).get(0);
        userResource.roles().clientLevel(service.getId()).add(Collections.singletonList(roleRepresentation));
    }

    private RolesResource getRoleResource() {
        ClientRepresentation service = keycloak.realm(this.realm).clients().findByClientId(clientId).get(0);
        ClientResource clientResource = keycloak.realm(this.realm).clients().get(service.getId());
        return clientResource.roles();
    }

    public UserRepresentation getUserById(String userId) {
        return getUsersResource().get(userId).toRepresentation();
    }

    private UsersResource getUsersResource() {
        RealmResource realm1 = keycloak.realm(this.realm);
        return realm1.users();
    }

    private UserResource getUserResource(String userId) {
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }

    public void emailVerification(String userId) {
        UserResource userResource = getUsersResource().get(userId);
        userResource.sendVerifyEmail(clientId, REDIRECT_URI);
    }

    public void forgotPassword(String userId) {
        UserRepresentation userRepresentation = getUserById(userId);
        if (userRepresentation != null) {
            UserResource userResource = getUsersResource().get(userRepresentation.getId());
            List<String> actions = new ArrayList<>();
            actions.add(UPDATE_PASSWORD);
            userResource.executeActionsEmail(clientId, REDIRECT_URI, actions);
        }
    }

    public List<UserRepresentation> getUsersByEmail(String email) {
        UsersResource usersResource = getUsersResource();
        return usersResource.searchByEmail(email, true);
    }

}
