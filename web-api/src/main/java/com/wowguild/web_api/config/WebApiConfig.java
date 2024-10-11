package com.wowguild.web_api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"com.wowguild.common"})
@EnableJpaRepositories(basePackages = "com.wowguild.common.repos")
@EntityScan(basePackages = "com.wowguild.common.entity")
public class WebApiConfig {

    @Bean
    public Gson getGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    @Bean
    public RestTemplate getRest() {
        return new RestTemplate();
    }
}
