package com.example.blendish.domain.user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TastePreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tastePreferenceId;

    private String taste;
    private Integer spicyLevel;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}