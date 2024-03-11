package ru.vitasoft.AnyService.authback.services;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.vitasoft.AnyService.authback.config.JwtProvider;
import ru.vitasoft.AnyService.back.dto.UserRegistrationRecord;
import ru.vitasoft.AnyService.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
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
    private final Keycloak service;

    @Bean
    public Keycloak service() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

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

    public void registration(UserRegistrationRecord user) {


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
        RealmResource realm1 = service.realm(this.realm);
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
    }

    public void assertRole(String userId, String roleName) {
        UserResource userResource = getUserResource(userId);

        RolesResource rolesResource = getRoleResource();
        RoleRepresentation roleRepresentation = rolesResource.get(roleName).toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

    private RolesResource getRoleResource() {
        return service.realm(this.realm).roles();
    }

    private UsersResource getUsersResource() {
        RealmResource realm1 = service.realm(this.realm);
        return realm1.users();
    }

    private UserResource getUserResource(String userId) {
        UsersResource usersResource = getUsersResource();
        return usersResource.get(userId);
    }

    public void emailVerification(String userId) {
        UsersResource usersResource = getUsersResource();
        usersResource.get(userId).sendVerifyEmail();
    }

}
