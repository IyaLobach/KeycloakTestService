package ru.vitasoft.AnyService.controller;

import lombok.AllArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vitasoft.AnyService.services.KeycloakUserService;

import java.security.Principal;



@RestController
@RequestMapping("test")
@AllArgsConstructor
public class AnyServiceController {

   private final KeycloakUserService keycloakUserService;


   // РАБОТАЕТ
    @GetMapping("/auth")
    public String hello3(@RequestParam String password, @RequestParam String username) {
        return "Auth user token  " +    keycloakUserService.checkUser(username, password);
    }

    @GetMapping("/all")
    public String hello4() {
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
