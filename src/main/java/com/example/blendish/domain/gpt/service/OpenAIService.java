package com.example.blendish.domain.gpt.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class OpenAIService {

    private final WebClient webClient;

    public OpenAIService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getGptResponse(String userMessage) {
        return webClient.post()
                .uri("/chat/completions")
                .header("Content-Type", "application/json")
                .bodyValue(Map.of(
                        "model", "gpt-4",
                        "messages", new Object[]{
                                Map.of("role", "system", "content", "You are a helpful assistant."),
                                Map.of("role", "user", "content", userMessage)
                        },
                        "max_tokens", 100
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    var choices = (java.util.List<Map<String, Object>>) response.get("choices");
                    if (choices != null && !choices.isEmpty()) {
                        return choices.get(0).get("message").toString();
                    }
                    return "No response from GPT.";
                });
    }
}