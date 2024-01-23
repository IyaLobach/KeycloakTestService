package ru.vitasoft.AnyService.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Beans {

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8080")
                .realm("PersonalCabinetTest")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("AnyServiceApi")
                .clientSecret("9jqJysANERPdRC1JW6XdCHa9V7Rt64Vl")
                .build();
    }
}
