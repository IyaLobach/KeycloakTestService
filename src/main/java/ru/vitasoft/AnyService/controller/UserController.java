package ru.vitasoft.AnyService.controller;

import lombok.AllArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.vitasoft.AnyService.dto.UserRegistrationRecord;
import ru.vitasoft.AnyService.services.KeycloakUserService;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("user")
@AllArgsConstructor
public class UserController {

    private final KeycloakUserService keycloakUserService;

    // работает с токеном сервиса. У клиента (сервиса) обязательно роль realm-management view-realm и realm-management manage-users
    @PostMapping
    public UserRegistrationRecord createUser(@RequestBody UserRegistrationRecord user) {
        return keycloakUserService.createUser(user);
    }

    // работает с токеном клиента
    @GetMapping
    public UserRepresentation getUser(Principal principal) {
        return keycloakUserService.getUserById(principal.getName());
    }

    // работает c токеном клиента
    @DeleteMapping("/{userId}")
    public HttpStatus deleteUser(@PathVariable String userId) {
        keycloakUserService.deleteUserById(userId);
        return HttpStatus.OK;
    }

    // работает с токеном сервиса. НЕ РАБОТАЕТ
    @PutMapping("/{username}")
    public HttpStatus updatePassword(@PathVariable String username) {
        keycloakUserService.forgotPassword(username);
        return HttpStatus.OK;
    }

    // работает с токеном сервиса
    @GetMapping("/findByName/{username}")
    public List<UserRepresentation> findByUsername(@PathVariable String username) {
        return keycloakUserService.getUserByUsername(username);
    }

}
