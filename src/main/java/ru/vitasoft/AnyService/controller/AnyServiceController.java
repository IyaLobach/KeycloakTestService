package ru.vitasoft.AnyService.controller;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;


@RestController
@RequestMapping("test")
public class AnyServiceController {

    // работает с токеном клиента
    @GetMapping("/all")
    public String hello3() {
        return "Hello from TestController - all";
    }

    // работает с токеном клиента
    @GetMapping("/operator")
    public ResponseEntity<String> hello(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        return ResponseEntity.ok("Hello from TestController - operator \nUser Name : " + userName + "\nUser Email : " + userEmail);
    }

    // работает с токеном клиента
    @GetMapping("/employee")
    public ResponseEntity<String> hello2(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        return ResponseEntity.ok("Hello from TestController - employee \nUser Name : " + userName + "\nUser Email : " + userEmail);
    }

}
