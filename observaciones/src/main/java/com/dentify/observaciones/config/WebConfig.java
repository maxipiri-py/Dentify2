package com.dentify.observaciones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// OBSERVACIONES (puerto 8084)
// Consume: Agenda (8083)

@Configuration
public class WebConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClientAgenda(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8083/api").build();
    }
}
