package com.example.blendish.domain.recipe.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public record Ingredient(String name, String amount) {
}
