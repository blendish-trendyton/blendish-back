package com.example.blendish.domain.recipe.dto;

import com.example.blendish.domain.comments.dto.CommentDTO;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityDetailDTO {
    private Long recipeId;
    private String foodImage;
    private String level;
    private String time;
    private Date postDate;
    private String userId;
    private String profilePic;
    private String name;
    private int spicyLevel;
    private boolean isHart;
    private boolean isScrap;
    private int crapCount;
    private int likeCount;
    private int commentCount;
    private String information;
    private List<CommentDTO> commentDTOList;
    private List<String> flavor;
}
