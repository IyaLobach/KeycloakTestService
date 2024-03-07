package ru.vitasoft.AnyService.back.model.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class Role implements GrantedAuthority {
    private String name;
    @Override
    public String getAuthority() {
        return String.format("ROLE_%s", name);
    }
}
