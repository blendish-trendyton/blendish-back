package com.example.blendish.domain.recipe.entity;

import com.example.blendish.domain.comments.entity.Comment;
import com.example.blendish.domain.foodflavor.entity.FoodFlavor;
import com.example.blendish.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Setter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 1)
    private String level;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    private List<Ingredient> ingredients;

    private int likeCount;

    @Column(nullable = false, length = 100)
    private String time;

    private int scrapCount;

    private int spicyLevel;

    @Column(length = 200)
    private String information;

    @Temporal(TemporalType.DATE)
    private Date postDate;

    @Temporal(TemporalType.DATE)
    private Date updatedDate;

    @Column(length = 255)
    private String foodImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeSteps> steps;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment ;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes ;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scrap> scraps ;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodFlavor> foodFlavors ;

    @PrePersist
    protected void onCreate() {
        this.postDate = new Date();
        this.updatedDate = this.postDate;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date();
    }

}
