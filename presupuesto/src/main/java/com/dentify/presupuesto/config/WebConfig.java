package com.dentify.presupuesto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// PRESUPUESTO (puerto 8089)
// Consume: Paciente (8081), Dentista (8082), Prestaciones (8086)

@Configuration
public class WebConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClientPaciente(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8081/api").build();
    }

    @Bean
    public WebClient webClientDentista(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8082/api").build();
    }

    @Bean
    public WebClient webClientPrestaciones(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8086/api").build();
    }
}
