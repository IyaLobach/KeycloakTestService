package ru.vitasoft.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("test")
@AllArgsConstructor
public class AnyServiceController {

    @GetMapping("/all")
    public String all() {
        return "Hello from TestController - all";
    }

    @GetMapping("/operator")
    public String operator(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;

        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        return "Hello from TestController - operator \nUser Name : " + userName + "\nUser Email : " + userEmail;
    }


    @GetMapping("/employee")
    public String employee(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        return "Hello from TestController - employee \nUser Name : " + userName + "\nUser Email : " + userEmail;
    }

}
