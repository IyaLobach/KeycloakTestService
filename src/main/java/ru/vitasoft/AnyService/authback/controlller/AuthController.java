package ru.vitasoft.AnyService.authback.controlller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.vitasoft.AnyService.authback.services.AuthService;
import ru.vitasoft.AnyService.back.dto.UserRegistrationRecord;
import ru.vitasoft.AnyService.model.User;

@RestController
@RequestMapping("authback")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    @GetMapping("/login")
    public HttpStatus auth(@RequestParam String password, @RequestParam String username, HttpServletResponse response) {
        authService.auth(username, password);
        return HttpStatus.OK;
    }

    @GetMapping("/identity")
    public User getUserIdentity() {
        return authService.getIdentity();
    }

    @PostMapping("/register")
    public void register(@RequestBody UserRegistrationRecord userRegistrationRecord) {
        authService.registration(userRegistrationRecord);
    }

    // с каким токеном???
    @PostMapping("/verifyEmail")
    public void verifyEmail(@RequestBody String userId) {
        authService.emailVerification(userId);
    }

}
