package com.example.blendish.domain.user.entity;


import com.example.blendish.domain.comments.entity.Comment;
import com.example.blendish.domain.recipe.entity.Likes;
import com.example.blendish.domain.recipe.entity.Scrap;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String userId;

    private String userPw, email,hometown,
            country,profilePic;

    private String role;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<TastePreference> tastePreferences;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments ;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes ;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scrap> scraps ;

}

