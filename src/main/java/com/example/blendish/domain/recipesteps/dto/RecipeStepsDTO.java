package com.example.blendish.domain.recipesteps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipeStepsDTO {
    private String details;
    private String stepImage;
    private int stepNum;
}
