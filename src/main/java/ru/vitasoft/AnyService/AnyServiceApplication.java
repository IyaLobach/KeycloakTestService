package ru.vitasoft.AnyService;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.security.Key;

@SpringBootApplication
public class AnyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnyServiceApplication.class, args);
	}

	// креды сервиса
	@Bean
	public Keycloak keycloak() {
		return KeycloakBuilder.builder()
				.serverUrl("http://localhost:8080")
				.realm("PersonalCabinetTest")
				.grantType(OAuth2Constants.CLIENT_CREDENTIALS)
				.clientId("AnyServiceApi")
				.clientSecret("bjV547DgTBZznSBAbbjcGiJMjiSBDYJR")
				.build();
	}

}
