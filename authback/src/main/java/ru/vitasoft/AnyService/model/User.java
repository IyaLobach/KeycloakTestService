package ru.vitasoft.AnyService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Builder
public class User {

    private String id;
    private String email;
    private Collection<GrantedAuthority> roles;
}
