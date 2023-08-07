package com.bid.idearush.domain.idea.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.request.IdeaRequest;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.exception.IdeaFindException;
import com.bid.idearush.global.exception.IdeaWriteException;
import com.bid.idearush.global.exception.UserFindException;
import com.bid.idearush.global.exception.errortype.IdeaFindErrorCode;
import com.bid.idearush.global.exception.errortype.IdeaWriteErrorCode;
import com.bid.idearush.global.exception.errortype.UserFindErrorCode;
import com.bid.idearush.global.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.bid.idearush.global.type.ServerIpAddress.IMAGE_BASE_PATH;

@Service
@RequiredArgsConstructor
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional
    public void update(Long userId, Long ideaId, IdeaRequest ideaRequest, MultipartFile image) {
        if(isMultipartFile(image) && !validateImage(image)) {
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

        String imageName = isMultipartFile(image) ?  image.getOriginalFilename() : idea.getImageName();

        s3Service.upload(IMAGE_BASE_PATH + "/"  + idea.getId(), imageName ,image);
        idea.updateOf(ideaRequest, imageName);
    }

    private boolean isMultipartFile(MultipartFile multipartFile){
        return !multipartFile.isEmpty() && multipartFile != null;
    }

    private boolean validateImage(MultipartFile image) {
        List<String> imageExtensions = List.of("image/jpeg", "image/png", "image/gif",
                "image/bmp", "image/tiff", "image/webp", "image/heif");

        return imageExtensions.contains(image.getContentType());
    }

    public void delete(Long userId, Long ideaId) {
        Idea idea = getIdea(ideaId);
        String filePath = IMAGE_BASE_PATH + "/" + idea.getId() + "/" + idea.getImageName();

        validateUser(userId, idea);

        s3Service.delete(filePath);
        ideaRepository.delete(idea);
    }

    private Idea getIdea(Long ideaId) {
        return ideaRepository.findById(ideaId).orElseThrow(
                () -> new IdeaFindException(IdeaFindErrorCode.IDEA_EMPTY));
    }

    private void validateUser(Long userId, Idea idea) {
        boolean isUser = userRepository.findById(userId).isPresent();

        if(!isUser) {
            throw new UserFindException(UserFindErrorCode.USER_EMPTY);
        }

        if(!idea.isAuthUser(userId)){
            throw new IdeaWriteException(IdeaWriteErrorCode.IDEA_UNAUTH);
        }
    }

}
