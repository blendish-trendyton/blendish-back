package com.example.blendish.domain.recipe.entity;

import com.example.blendish.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recipeId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 1)
    private String level;

    @Column(nullable = false, length = 100)
    private String ingredients;

    private int likeCount;

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
