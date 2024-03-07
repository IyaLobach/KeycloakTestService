package ru.vitasoft.AnyService.config;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.nimbusds.jwt.*;

@Component
public class TokenConverter implements Converter<String, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${keycloak.client-id}")
    private String clientId;

    @Override
    public AbstractAuthenticationToken convert(String token) {
        try {
            Jwt jwt = parseJwt(token);
            Collection<GrantedAuthority> authorities = Stream.concat(
                    jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                    extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
            return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private Jwt parseJwt(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        Map<String, Object> claims = claimsSet.getClaims();
        JWSHeader headers = signedJWT.getHeader();
        return new Jwt(token, null, null, headers.toJSONObject(), claimsSet.getClaims());
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        Collection<String> allRoles = new ArrayList<>();
        Collection<String> resourceRoles;

        if (resourceAccess != null) {
            Map<String, Object> account = (Map<String, Object>) resourceAccess.get(clientId);
            if (account.containsKey("roles")) {
                resourceRoles = (Collection<String>) account.get("roles");
                allRoles.addAll(resourceRoles);
            }
        }

        return allRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}