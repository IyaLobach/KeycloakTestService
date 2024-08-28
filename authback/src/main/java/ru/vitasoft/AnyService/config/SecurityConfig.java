package ru.vitasoft.AnyService.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import ru.vitasoft.AnyService.handler.MyAuthSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MyAuthSuccessHandler myAuthSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/operator/**").hasRole("OPERATOR")
                            .requestMatchers("/employee/**").hasRole("EMPLOYEE")
                            .anyRequest().authenticated();
                })
                .oauth2Login(oauthlogin -> oauthlogin.successHandler(myAuthSuccessHandler))
                .build();
    }

}