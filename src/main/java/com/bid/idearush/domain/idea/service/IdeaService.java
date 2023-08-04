package com.bid.idearush.domain.idea.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.request.IdeaRequest;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public void update(Long userId, Long ideaId, IdeaRequest ideaRequest, MultipartFile image) {
        boolean isUser = userRepository.findById(userId).isPresent();

        if(!isUser) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }

        Idea idea = ideaRepository.findById(ideaId).orElseThrow(
                () -> new IllegalArgumentException("아이디어가 존재하지 않습니다."));

        if(userId != idea.getUsers().getId()){
            throw new IllegalArgumentException("아이디어에 권한이 없습니다.");
        }

        String imageName = (!image.isEmpty() && image != null) ? idea.getImageName() : image.getOriginalFilename();

        s3Service.upload(image);
        idea.updateOf(ideaRequest, imageName);
    }
}
