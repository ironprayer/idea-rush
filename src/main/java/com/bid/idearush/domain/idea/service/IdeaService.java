package com.bid.idearush.domain.idea.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.request.IdeaRequest;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public void update(Long userId, Long ideaId, IdeaRequest ideaRequest, MultipartFile image) {
        if(!validateImage(image)) {
            throw new IllegalArgumentException("이미지 파일이 아닙니다.");
        }

        boolean isUser = userRepository.findById(userId).isPresent();

        if(!isUser) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }

        Idea idea = ideaRepository.findById(ideaId).orElseThrow(
                () -> new IllegalArgumentException("아이디어가 존재하지 않습니다."));

        if(userId != idea.getUsers().getId()){
            throw new IllegalArgumentException("아이디어에 권한이 없습니다.");
        }

        String imageName = (!image.isEmpty() && image != null) ?  image.getOriginalFilename() : idea.getImageName();

        s3Service.upload("idea/image" + idea.getId().toString(), imageName ,image);
        idea.updateOf(ideaRequest, imageName);
    }

    private boolean validateImage(MultipartFile image) {
        List<String> imageExtensions = List.of("image/jpeg", "image/png", "image/gif",
                "image/bmp", "image/tiff", "image/webp", "image/heif");

        return imageExtensions.contains(image.getContentType());
    }

}
