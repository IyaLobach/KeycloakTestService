package ru.vitasoft.AnyService.authback.controlller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vitasoft.AnyService.authback.services.AuthService;
import ru.vitasoft.AnyService.services.KeycloakUserService;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    @GetMapping()
    public String auth(@RequestParam String password, @RequestParam String username, HttpServletResponse response) {
        String authToken = authService.auth(username, password);
        Cookie cookie = new Cookie("IYA", authToken);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "Auth user token  " + authToken;
    }
}
