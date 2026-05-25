package com.dentify.presupuesto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// PRESUPUESTO (puerto 8087)
// Consume: Pago (8088), Reporte (8089)

@Configuration
public class WebConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClientPago(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8088/api").build();
    }

    @Bean
    public WebClient webClientReporte(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8089/api").build();
    }
}
