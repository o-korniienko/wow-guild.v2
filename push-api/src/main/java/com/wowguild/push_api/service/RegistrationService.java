package com.wowguild.push_api.service;

import com.wowguild.common.entity.User;
import com.wowguild.common.service.entity.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;

    public Mono<ServerResponse> registerDevice(ServerRequest serverRequest) {
        /*return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    String username = authentication.getName();
                    User user = userService.findByUsername(username);

                    serverRequest.bodyToMono(String.class)
                            .flatMap(body -> {
                                System.out.println(user);
                                System.out.println(body);
                                return Mono.empty();
                            });

                    return ServerResponse.ok()
                            .contentType(MediaType.TEXT_PLAIN)
                            .bodyValue("User " + username + " has registered a device");
                });*/

        Mono<Authentication> authUserMono = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication);

        Mono<String> requestBodyMono = serverRequest.bodyToMono(String.class);

        return authUserMono.zipWith(requestBodyMono)
                .flatMap(tuple -> {
                    Authentication auth = tuple.getT1();
                    String deviceRequest = tuple.getT2();

                    String username = auth.getName();
                    User user = userService.findByUsername(username);

                    System.out.println(deviceRequest);
                    System.out.println(user);

                    return ServerResponse.ok()
                            .contentType(MediaType.TEXT_PLAIN)
                            .body(BodyInserters.fromValue("User " + username + " registered device:"));
                })
                .switchIfEmpty(ServerResponse.badRequest().build());
    }
}
