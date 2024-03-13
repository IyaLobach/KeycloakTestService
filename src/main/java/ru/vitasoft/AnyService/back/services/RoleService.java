package ru.vitasoft.AnyService.back.services;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;
import ru.vitasoft.AnyService.back.services.interfaces.RoleServiceI;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class RoleService implements RoleServiceI {

    private final Keycloak keycloak;
    private final String realm = "PersonalCabinetTest";
    private final KeycloakUserService keycloakUserService;


    @Override
    public void assertRole(String userId, String roleName) {
        UserResource userResource = keycloakUserService.getUserResource(userId);

        RolesResource rolesResource = getRoleResource();
        RoleRepresentation roleRepresentation = rolesResource.get(roleName).toRepresentation();
        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

    private RolesResource getRoleResource() {
        return keycloak.realm(this.realm).roles();
    }
}
