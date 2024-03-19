package ru.vitasoft.AnyService.controlller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.vitasoft.AnyService.model.dto.UserDto;
import ru.vitasoft.AnyService.services.AuthService;
import ru.vitasoft.AnyService.model.User;

import java.util.List;

@RestController
@RequestMapping("authback")
@RequiredArgsConstructor
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
    public void register(@RequestBody UserDto userRegistrationRecord) {
        authService.registration(userRegistrationRecord);
    }

    @PostMapping("/verifyEmail")
    public void verifyEmail(@RequestBody UserDto userDto) {
        authService.emailVerification(userDto.getId());
    }

    @PostMapping("/forgotPassword")
    public void forgotPassword(@RequestBody UserDto userDto) {
        authService.forgotPassword(userDto.getId());
    }

    @PostMapping("/findByEmail")
    public List<UserRepresentation> findByEmail(@RequestBody UserDto userDto) {
        return authService.getUsersByEmail(userDto.getEmail());
    }

    @PostMapping("/findById")
    public UserRepresentation findById(@RequestBody UserDto userDto) {
        return authService.getUserById(userDto.getId());
    }

    @PostMapping("/assertRole")
    public void assertRole(@RequestBody UserDto userDto) {
        authService.assertRole(userDto.getId(), userDto.getRole());
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }




}
