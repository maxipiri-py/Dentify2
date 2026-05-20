package com.dentify.dentista.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    // 1. Definimos el Builder primero
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean// anotacion para 
    public WebClient webClientPaciente(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8081/api").build(); // Puerto del Micro de Paciente
    }

    @Bean
    public WebClient webClientDentista(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8082/api").build(); // Puerto del Micro de Médicos
    }
}
