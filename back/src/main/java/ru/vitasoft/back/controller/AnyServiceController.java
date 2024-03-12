package ru.vitasoft.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vitasoft.back.services.KeycloakUserService;

import java.security.Principal;



@RestController
@RequestMapping("test")
@AllArgsConstructor
public class AnyServiceController {

   private final KeycloakUserService keycloakUserService;


    @GetMapping("/all")
    public String hello4() {
        return "Hello from TestController - all";
    }

    // работает с токеном клиента
    @GetMapping("/operator")
    public String hello(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;

        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        return "Hello from TestController - operator \nUser Name : " + userName + "\nUser Email : " + userEmail;
    }

    // работает с токеном клиента
    @GetMapping("/employee")
    public String hello2(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        return "Hello from TestController - employee \nUser Name : " + userName + "\nUser Email : " + userEmail;
    }

}
