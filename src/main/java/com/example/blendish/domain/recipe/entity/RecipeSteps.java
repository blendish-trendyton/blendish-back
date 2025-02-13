package com.example.blendish.domain.recipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSteps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rsId;

    @Column(nullable = false)
    private int stepNum;

    @Column(length = 200)
    private String details;

    @Column(length = 255)
    private String stepImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId", nullable = false)
    private Recipe recipe;

    public void updateRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
