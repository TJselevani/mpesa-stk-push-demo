package com.example.mpesa.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://sandbox.safaricom.co.ke") // default base URL (optional)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
