package com.wowguild.security;


import com.wowguild.common.service.UserService;
import com.wowguild.push_api.security.AuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

import static com.wowguild.arguments.UserGenerator.generateUser;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AuthenticationFilterTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    @Mock
    private ServerWebExchange exchange;
    @Mock
    private ServerHttpRequest request;
    @Mock
    private ServerHttpResponse response;
    @Mock
    private WebFilterChain chain;

    private AuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new AuthenticationFilter(passwordEncoder, userService);
    }

    @ParameterizedTest
    @MethodSource("filterArgs")
    void filterTest(HttpHeaders headers, boolean matches) {
        when(exchange.getRequest()).thenReturn(request);
        when(request.getHeaders()).thenReturn(headers);

        if (!matches) {
            when(exchange.getResponse()).thenReturn(response);
        } else {
            when(userService.findByUsername(anyString())).thenReturn(generateUser("user"));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(matches);
            when(chain.filter(exchange)).thenReturn(Mono.empty());
        }

        filter.filter(exchange, chain);
    }

    static Stream<Arguments> filterArgs() {
        HttpHeaders headers1 = new HttpHeaders();
        headers1.add("testKey", "user-pass");
        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("testKey", "");
        HttpHeaders headers3 = new HttpHeaders();
        headers3.add("testKey", "userpass");
        HttpHeaders headers4 = new HttpHeaders();

        return Stream.of(
                Arguments.of(headers1, true),
                Arguments.of(headers2, false),
                Arguments.of(headers3, false),
                Arguments.of(headers4, false)
        );
    }

}
