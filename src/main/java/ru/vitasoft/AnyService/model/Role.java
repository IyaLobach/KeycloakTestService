package ru.vitasoft.AnyService.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Data
@Component
public class Role implements GrantedAuthority {

    private String name;
    @Override
    public String getAuthority() {
        return String.format("ROLE_%s", name);
    }
}
