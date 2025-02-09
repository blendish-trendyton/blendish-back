package com.example.blendish.domain.user.entity;


import jakarta.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private Long userId;

    private String userPw;
    private String eMail;
    private String hometown;
    private String country;
    private String profilePic;
}
