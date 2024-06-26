package com.wowguild.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WowGuildConfig {

    @Bean
    public Gson getGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public RestTemplate getRest() {
        return new RestTemplate();
    }
}
