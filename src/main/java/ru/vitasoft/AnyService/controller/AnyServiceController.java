package ru.vitasoft.AnyService.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequestMapping("test")
public class AnyServiceController {

    @GetMapping("/all")
    public String hello3() {
        return "Hello from TestController - all";
    }

    @GetMapping("/operator")
    public ResponseEntity<String> hello(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        return ResponseEntity.ok("Hello from TestController - operator \nUser Name : " + userName + "\nUser Email : " + userEmail);
    }

    @GetMapping("/employee")
    public ResponseEntity<String> hello2(Principal principal) {
        JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
        String userName = (String) token.getTokenAttributes().get("name");
        String userEmail = (String) token.getTokenAttributes().get("email");
        return ResponseEntity.ok("Hello from TestController - employee \nUser Name : " + userName + "\nUser Email : " + userEmail);
    }


}
