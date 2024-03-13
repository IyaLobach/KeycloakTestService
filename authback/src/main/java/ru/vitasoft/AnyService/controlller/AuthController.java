package ru.vitasoft.AnyService.controlller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.vitasoft.AnyService.model.dto.UserDto;
import ru.vitasoft.AnyService.model.dto.UserRegistrationRecord;
import ru.vitasoft.AnyService.services.AuthService;
import ru.vitasoft.AnyService.model.User;

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
    public void register(@RequestBody UserRegistrationRecord userRegistrationRecord) {
        authService.registration(userRegistrationRecord);
    }


    @PostMapping("/verifyEmail")
    public void verifyEmail(@RequestBody UserDto userDto) {
        authService.emailVerification(userDto.getId());
    }
}
