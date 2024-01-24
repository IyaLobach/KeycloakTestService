package ru.vitasoft.AnyService.controller;

import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.vitasoft.AnyService.dto.UserRegistrationRecord;
import ru.vitasoft.AnyService.services.KeycloakUserService;

import java.security.Principal;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {

    private final KeycloakUserService keycloakUserService;

    @PostMapping
    public UserRegistrationRecord createUser(@RequestBody UserRegistrationRecord user) {
        return keycloakUserService.createUser(user);
    }


    @GetMapping
    public UserRepresentation getUser(Principal principal) {
        return keycloakUserService.getUserById(principal.getName());
    }

    @DeleteMapping("/{userId}")
    public HttpStatus deleteUser(@PathVariable String userId) {
        keycloakUserService.deleteUserById(userId);
        return HttpStatus.OK;
    }

    @PutMapping("/{username}")
    public HttpStatus updatePassword(@PathVariable String username) {
        keycloakUserService.forgotPassword(username);
        return HttpStatus.OK;
    }


}
