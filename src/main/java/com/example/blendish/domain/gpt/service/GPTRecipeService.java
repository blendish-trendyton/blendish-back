package com.example.blendish.domain.gpt.service;

import com.example.blendish.domain.gpt.dto.CustomRecipeReqDTO;
import com.example.blendish.domain.gpt.dto.CustomRecipeResDTO;
import com.example.blendish.domain.user.entity.User;
import com.example.blendish.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GPTRecipeService {

    private static final Logger logger = LoggerFactory.getLogger(GPTRecipeService.class);
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-4o";
    private static final String SYSTEM_MESSAGE = "너는 요리 레시피 추천 AI야. 요청에 따라 요리를 추천해줘.";

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    public CustomRecipeResDTO getAiGeneratedRecipe(CustomRecipeReqDTO request, String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }

        String prompt = generateRecipePrompt(request, user.getCountry());

        Map<String, Object> requestBody = Map.of(
                "model", MODEL,
                "messages", List.of(
                        Map.of("role", "system", "content", SYSTEM_MESSAGE),
                        Map.of("role", "user", "content", prompt)
                ),
                "temperature", 0.7
        );

        HttpHeaders headers = createHeaders();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    OPENAI_API_URL, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {}
            );

            Map<String, Object> responseBody = responseEntity.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> firstChoice = choices.get(0);
                    Map<String, String> message = (Map<String, String>) firstChoice.get("message");
                    String recipeContent = message.getOrDefault("content", "레시피를 생성하는 데 실패했습니다.");
                    return new CustomRecipeResDTO(request, recipeContent);
                }
            }
        } catch (HttpClientErrorException e) {
            logger.error("OpenAI API Error: StatusCode = {}, ResponseBody = {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            return new CustomRecipeResDTO(request, "레시피 생성 중 API 오류 발생: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Unexpected error calling OpenAI API", e);
            return new CustomRecipeResDTO(request, "레시피 생성 중 오류 발생: " + e.getMessage());
        }

        return new CustomRecipeResDTO(request, "레시피를 생성하는 중 오류가 발생했습니다.");
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);
        return headers;
    }

    private String generateRecipePrompt(CustomRecipeReqDTO request, String country) {
        return String.format("""
            %s 국가에서 쉽게 구할 수 있는 재료를 활용하여 %s, %s 맛의 %s분 내 조리 가능한 %s 요리를 추천해줘.
            맵기 레벨 %d의 4가지 레시피를 제공해줘.
            각 레시피는 다음 형식으로:
            1. [레시피 이름]
            - 조리 시간: [시간]분
            - 난이도: [쉬움/보통/어려움]
            - 요약: [레시피의 조리방법 요약]
            - 재료: [재료 목록 및 계량]
            - 재료 팁: [%s에서 재료를 구할 수 있는 팁]
            - 조리 순서 (단계별로 설명):
            """,
                country,
                String.join(", ", request.tastes()),
                request.difficulty(),
                request.cookingTime(),
                request.category(),
                request.spiceLevel(),
                country
        );
    }
}
