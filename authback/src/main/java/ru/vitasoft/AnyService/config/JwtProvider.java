package ru.vitasoft.AnyService.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.util.StringUtils.hasText;

@Data
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private static String TOKEN = "IYA";
    private final TokenConverter tokenConverter;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public JwtAuthenticationToken getTokenFromRequest() {
        String bearer = request.getHeader(COOKIE);
        if (hasText(bearer) && bearer.contains(TOKEN)) {
            try {
                return (JwtAuthenticationToken) tokenConverter.convert(getTokenFromCookiesHeader(bearer));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private String getTokenFromCookiesHeader(String bearer) {
        StringBuilder resultToken = new StringBuilder();
        String cookie = bearer.substring(bearer.indexOf(TOKEN) + TOKEN.length() + 1);
        for (char c : cookie.toCharArray()) {
            if (c == ';') {
                break;
            }
            resultToken.append(c);
        }
        return resultToken.toString();
    }

    public void setCookie(String token) {
        Cookie cookie = new Cookie(TOKEN, token);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
