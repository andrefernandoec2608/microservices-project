package com.andre.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.client.base-url}")
    private String clientServiceBaseUrl;

    @Value("${services.account.base-url}")
    private String accountServiceBaseUrl;

    @Bean
    public WebClient clientServiceWebClient() {
        return WebClient.builder()
                .baseUrl(clientServiceBaseUrl)
                .build();
    }

    @Bean
    public WebClient accountServiceWebClient() {
        return WebClient.builder()
                .baseUrl(accountServiceBaseUrl)
                .build();
    }
}