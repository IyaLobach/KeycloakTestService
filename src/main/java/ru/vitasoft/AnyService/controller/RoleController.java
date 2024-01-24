package ru.vitasoft.AnyService.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.vitasoft.AnyService.services.RoleService;

@RestController
@RequestMapping("role")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PutMapping("/{userId}/{roleName}")
    public String assertRole(@PathVariable  String userId, @PathVariable String roleName) {
        roleService.assertRole(userId, roleName);
        return "OK";
    }
}
