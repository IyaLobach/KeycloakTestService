package ru.vitasoft.AnyService.back.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.vitasoft.AnyService.config.TokenConverter;

import java.io.IOException;
import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.util.StringUtils.hasText;

@Order(1)
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static String TOKEN = "IYA=";
    private final TokenConverter tokenConverter;

    private String getTokenFromCookiesHeader(String bearer) throws JsonProcessingException {
        StringBuilder resultToken = new StringBuilder();
        String cookie = bearer.substring(bearer.indexOf(TOKEN) + TOKEN.length());
        for (char c : cookie.toCharArray()) {
            if (c == ';') {
                break;
            }
            resultToken.append(c);
        }
        return resultToken.toString();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (token != null) {
            SecurityContextHolder.getContext().setAuthentication(tokenConverter.convert(token));
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
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