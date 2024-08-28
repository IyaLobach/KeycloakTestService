package ru.vitasoft.AnyService.controlller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class HelloController {
    @GetMapping()
    public ResponseEntity<String> auth() {
        return new ResponseEntity<>("Hello from AnyService", HttpStatus.OK);
    }

}
