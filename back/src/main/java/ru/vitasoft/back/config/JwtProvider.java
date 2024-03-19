package ru.vitasoft.back.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.util.StringUtils.hasText;

@Component
public class JwtProvider {

    public static String TOKEN = "IYA";

    private String getTokenFromCookiesHeader(String bearer) throws JsonProcessingException {
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

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(COOKIE);
        if (hasText(bearer) && bearer.contains(TOKEN)) {
            try {
                return getTokenFromCookiesHeader(bearer);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
