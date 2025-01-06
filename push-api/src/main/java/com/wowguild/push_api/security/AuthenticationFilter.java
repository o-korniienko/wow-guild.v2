package com.wowguild.push_api.security;

import com.wowguild.common.entity.security.User;
import com.wowguild.common.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@AllArgsConstructor
public class AuthenticationFilter implements WebFilter {

    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String token = extractToken(headers);

        if (token != null) {
            Authentication authentication = getAuthenticationFromToken(token);
            if (authentication != null) {
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            }
        }

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private String extractToken(HttpHeaders headers) {
        if (headers.get("testKey") != null) {
            return headers.get("testKey").get(0);
        }
        return null;
    }

    private Authentication getAuthenticationFromToken(String token) {
        if (!token.isEmpty() && token.contains("-")) {
            String name = token.split("-")[0];
            String password = token.split("-")[1];

            User user = userService.findByUsername(name);

            if (user != null && passwordEncoder.matches(password, user.getPassword())) {
                return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
