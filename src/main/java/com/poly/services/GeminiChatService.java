package com.poly.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;

@Service
public class GeminiChatService {

    private final WebClient webClient;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiChatService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getChatResponse(String userMessage) {
        return webClient.post()
                .uri("https://api.gemini.example.com/v1/chat") // URL của Gemini API
                .header("Authorization", "Bearer " + apiKey)
                .bodyValue("{ \"message\": \"" + userMessage + "\" }")
                .retrieve()
                .bodyToMono(String.class);
    }
}
