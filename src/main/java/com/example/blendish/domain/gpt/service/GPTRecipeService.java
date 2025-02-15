package com.example.blendish.domain.gpt.service;

import com.example.blendish.domain.gpt.dto.CustomRecipeReqDTO;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
public class GPTRecipeService {

    private static final Logger logger = LoggerFactory.getLogger(GPTRecipeService.class);
    private final WebClient webClient;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    public Mono<String> getAiGeneratedRecipe(CustomRecipeReqDTO request, @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        User user = userRepository.findByUserId(userId);

        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }

        String prompt = generateRecipePrompt(request, user.getCountry());


        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4",
                "messages", List.of(
                        Map.of("role", "system", "content", "너는 요리 레시피 추천 AI야. 요청에 따라 요리를 추천해줘."),
                        Map.of("role", "user", "content", prompt.replaceAll("\\[|\\]", ""))
                ),
                "temperature", 0.7
        );


        try {
            String requestJson = objectMapper.writeValueAsString(requestBody);
            logger.info("Sending request to OpenAI: {}", requestJson);
        } catch (Exception e) {
            logger.error("Error converting request to JSON", e);
        }
        return Mono.delay(Duration.ofSeconds(3))  // 3초 딜레이 추가
                .then(webClient.post()
                        .uri("/chat/completions")
                        .header("Authorization", "Bearer " + openAiApiKey)
                        .header("Content-Type", "application/json")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                        .bodyValue(requestBody)
                        .retrieve()
                        .bodyToMono(Map.class)
                        .doOnNext(response -> logger.info("Response from OpenAI: {}", response))
                        .onErrorResume(WebClientResponseException.class, e -> {
                            logger.error("OpenAI API Error: StatusCode = {}, ResponseBody = {}",
                                    e.getStatusCode(), e.getResponseBodyAsString());
                            return Mono.just(Map.of("error", "OpenAI API 요청 중 오류가 발생했습니다."));
                        })
                        .retryWhen(Retry.backoff(3, Duration.ofSeconds(3))
                                .maxBackoff(Duration.ofSeconds(10))
                                .filter(throwable -> !(throwable instanceof WebClientResponseException &&
                                        ((WebClientResponseException) throwable).getStatusCode().value() == 421)))
                        .map(response -> {
                            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                            if (choices != null && !choices.isEmpty()) {
                                Map<String, Object> firstChoice = choices.get(0);
                                Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                                return (String) message.get("content");
                            }
                            return "레시피를 생성하는 데 실패했습니다.";
                        }));


    }

    private String generateRecipePrompt(CustomRecipeReqDTO request, String country) {
        return String.format("""
        %s 국가에서 쉽게 구할 수 있는 재료를 활용하여 %s, %s 맛의 %d분 내 조리 가능한 %s 요리를 추천해줘.
        맵기 레벨 %d의 4가지 레시피를 제공해줘.
        각 레시피는 다음 형식으로:
        1. [레시피 이름]
        - 조리 시간: [시간]분
        - 난이도: [쉬움/보통/어려움]
        - 특징: [레시피의 특징]
        - 재료 팁: [%s에서 재료를 구할 수 있는 팁]
        - 조리 순서 (4단계): 
        """,
                country, request.tastes(), request.difficulty(), request.cookingTime(), request.category(), request.spiceLevel(), country
        );
    }
}
