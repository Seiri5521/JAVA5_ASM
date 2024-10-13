package com.poly.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AiChatService {

    private WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    public AiChatService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
    }

    public String askQuestion(String question) {
        String response = webClient.post()
            .uri("/chat/completions")
            .header("Authorization", "Bearer " + apiKey)
            .bodyValue(buildRequestBody(question))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        return response;
    }

    private Map<String, Object> buildRequestBody(String prompt) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(new HashMap<>(Map.of("role", "system", "content", "You are a helpful assistant.")));
        messages.add(new HashMap<>(Map.of("role", "user", "content", prompt)));
        
        requestBody.put("messages", messages);
        return requestBody;
    }

}