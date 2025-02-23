package com.example.blendish.domain.recipe.service;

import com.example.blendish.global.s3.S3UploadService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RecipeImageService {

    private final S3UploadService s3UploadService;

    public String uploadImage(MultipartFile image) throws IOException {
        return (image != null && !image.isEmpty()) ? s3UploadService.saveFile(image) : null;
    }

    public List<String> uploadStepImages(List<MultipartFile> stepImages) {
        if (stepImages == null || stepImages.isEmpty()) {
            return new ArrayList<>();
        }
        return stepImages.stream()
                .map(img -> {
                    try {
                        return s3UploadService.saveFile(img);
                    } catch (IOException e) {
                        throw new RuntimeException("이미지 업로드 실패", e);
                    }
                })
                .toList();
    }
}

