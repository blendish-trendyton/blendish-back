package com.example.blendish.domain.gpt.dto;

import java.util.List;

public record GPTRequestDTO(String model, List<MessageDTO> messages) {
}
