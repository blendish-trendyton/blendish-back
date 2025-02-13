package com.example.blendish.domain.foodflavor.entity;

import com.example.blendish.domain.recipe.entity.Recipe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FoodFlavor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tasteId;

    @Column(nullable = false)
    private String taste;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId", nullable = false)
    private Recipe recipe;

}
