package com.wowguild.push_api.config;

import com.wowguild.push_api.service.RegistrationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> getMessageRouter(RegistrationService registrationService) {
        return RouterFunctions.route()
                .path("/register-device", builder -> builder
                        .POST(registrationService::registerDevice))
                .build();
    }
}
