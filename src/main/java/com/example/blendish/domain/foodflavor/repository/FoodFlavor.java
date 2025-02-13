package com.example.blendish.domain.foodflavor.repository;

import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.user.entity.User;
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
