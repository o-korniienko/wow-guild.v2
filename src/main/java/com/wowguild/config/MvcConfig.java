package com.wowguild.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {


    @Bean
    public RestTemplate getRest() {
        return new RestTemplate();
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        //configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        List<String> settings = new ArrayList<>();
        settings.add("Access-Control-Allow-Origin");
        settings.add("Access-Control-Allow-Methods");
        settings.add("Access-Control-Allow-Headers");
        settings.add("Access-Control-Max-Age");
        settings.add("Access-Control-Request-Headers");
        settings.add("Access-Control-Request-Method");
      /*  configuration.setExposedHeaders(Collections.addAll("Access-Control-Allow-Origin", "Access-Control-Allow-Methods", "Access-Control-Allow-Headers", "Access-Control-Max-Age",
                "Access-Control-Request-Headers", "Access-Control-Request-Method"));*/
        configuration.setExposedHeaders(settings);


        configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /*@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
        registry.addMapping("/**").allowedOrigins("http://localhost:3000");
    }*/
}
