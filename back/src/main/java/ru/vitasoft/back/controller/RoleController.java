package ru.vitasoft.back.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.vitasoft.back.services.RoleService;

@RestController
@RequestMapping("role")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // Работает. Нужен токен сервиса. У клиента (сервиса) обязательно роль realm-management view-realm и realm-management manage-users
    @PutMapping("/{userId}/{roleName}")
    public String assertRole(@PathVariable  String userId, @PathVariable String roleName) {
        roleService.assertRole(userId, roleName);
        return "OK";
    }
}
