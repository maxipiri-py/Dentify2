package com.dentify.pagos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {
    @Bean
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
    @Bean// anotacion para 
    public WebClient webClientPaciente(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8081/api").build(); // Puerto del Micro de Paciente
    }

    @Bean
    public WebClient webClientMedicos(WebClient.Builder builder) {
        return builder.baseUrl("http://localhost:8082/api").build(); // Puerto del Micro de Médicos
    }
}
