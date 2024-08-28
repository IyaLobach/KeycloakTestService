package ru.vitasoft.AnyService.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MyAuthSuccessHandler implements AuthenticationSuccessHandler {
    public static final String TOKEN = "HELLO_TOKEN";

    private final OAuth2AuthorizedClientRepository authorizedClientRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();

        OAuth2AuthorizedClient authorizedClient = authorizedClientRepository.loadAuthorizedClient(
                clientRegistrationId, oauthToken, request);

        if (authorizedClient != null) {
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

            if (accessToken != null) {
                Cookie cookie = makeCookie(accessToken.getTokenValue());
                response.addCookie(cookie);
            }
        }
    }
    private Cookie makeCookie(String token) {
        Cookie cookie = new Cookie(TOKEN, token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}