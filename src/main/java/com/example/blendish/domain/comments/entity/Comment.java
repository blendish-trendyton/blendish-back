package com.example.blendish.domain.comments.entity;

import com.example.blendish.domain.recipe.entity.Recipe;
import com.example.blendish.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false, length = 100)
    private String content;

    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentCommentId", nullable = true, insertable = false, updatable = false)
    private Comment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId", nullable = false)
    private Recipe recipe;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }
}
