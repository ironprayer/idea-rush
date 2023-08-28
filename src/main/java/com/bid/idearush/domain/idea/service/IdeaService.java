package com.bid.idearush.domain.idea.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.reponse.IdeaFindAllResponse;
import com.bid.idearush.domain.idea.model.reponse.IdeaListResponse;
import com.bid.idearush.domain.idea.model.request.IdeaRequest;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.exception.FileWriteException;
import com.bid.idearush.global.exception.IdeaFindException;
import com.bid.idearush.global.exception.IdeaWriteException;
import com.bid.idearush.global.exception.UserFindException;
import com.bid.idearush.global.exception.errortype.FileWriteErrorCode;
import com.bid.idearush.global.exception.errortype.IdeaFindErrorCode;
import com.bid.idearush.global.exception.errortype.IdeaWriteErrorCode;
import com.bid.idearush.global.exception.errortype.UserFindErrorCode;
import com.bid.idearush.global.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.bid.idearush.global.type.ServerIpAddress.IMAGE_BASE_PATH;

@Service
@RequiredArgsConstructor
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public IdeaListResponse findOneIdea(Long ideaId) {
        return ideaRepository.findIdeaOne(ideaId)
                .orElseThrow(() -> {
                    throw new IdeaFindException(IdeaFindErrorCode.IDEA_EMPTY);
                });
    }

    @Transactional(readOnly = true)
    public IdeaFindAllResponse findAllIdea(String keyword, Category category, Integer page) {

        if (StringUtils.hasText(keyword) && !Objects.isNull(category)) {
            throw new IdeaFindException(IdeaFindErrorCode.KEYWORD_CATEGORY_SAME);
        }

        IdeaFindAllResponse findList;
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(page, 10, sort);

        if (!StringUtils.hasText(keyword) && Objects.isNull(category)) {
            findList = ideaRepository.findIdeaAll(pageable);
        } else {
            findList = ideaRepository.findCategoryAndTitleAll(category, keyword, pageable);
        }
        return findList;
    }

    @Transactional
    public void update(Long userId, Long ideaId, IdeaRequest ideaRequest, MultipartFile image) {
        Idea idea = getIdea(ideaId);
        String imageName = idea.getImageName();
        validateUser(userId, idea);

        if (isMultipartFile(image)) {
            if (!validateImage(image)) {
                throw new FileWriteException(FileWriteErrorCode.NOT_IMAGE);
            }

            imageName = image.getOriginalFilename();
            s3Service.upload(IMAGE_BASE_PATH + "/" + idea.getId(), imageName, image);
        }

        idea.updateOf(ideaRequest, imageName);
    }

    @Transactional
    public void createIdea(IdeaRequest ideaRequest, MultipartFile image, Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(
                () -> new UserFindException(UserFindErrorCode.USER_EMPTY));

        String imageName = null;
        if (isMultipartFile(image)) {
            if (!validateImage(image)) {
                throw new FileWriteException(FileWriteErrorCode.NOT_IMAGE);
            }
            imageName = image.getOriginalFilename();
        }

        Idea newIdea = ideaRequest.toIdea(user, imageName);
        ideaRepository.save(newIdea);

        if (imageName != null) {
            String uploadPath = IMAGE_BASE_PATH + "/" + newIdea.getId();
            s3Service.upload(uploadPath, imageName, image);
        }
    }

    public void deleteIdea(Long userId, Long ideaId) {
        Idea idea = getIdea(ideaId);
        String filePath = IMAGE_BASE_PATH + "/" + idea.getId() + "/" + idea.getImageName();

        validateUser(userId, idea);

        s3Service.delete(filePath);
        ideaRepository.delete(idea);
    }

    private boolean isMultipartFile(MultipartFile multipartFile) {
        return multipartFile != null && !multipartFile.isEmpty();
    }

    private boolean validateImage(MultipartFile image) {
        List<String> imageExtensions = List.of("image/jpeg", "image/png", "image/gif",
                "image/bmp", "image/tiff", "image/webp", "image/heif");

        return imageExtensions.contains(image.getContentType());
    }

    private Idea getIdea(Long ideaId) {
        return ideaRepository.findById(ideaId).orElseThrow(
                () -> new IdeaFindException(IdeaFindErrorCode.IDEA_EMPTY));
    }

    private void validateUser(Long userId, Idea idea) {
        boolean isUser = userRepository.findById(userId).isPresent();

        if (!isUser) {
            throw new UserFindException(UserFindErrorCode.USER_EMPTY);
        }

        if (!idea.isAuthUser(userId)) {
            throw new IdeaWriteException(IdeaWriteErrorCode.IDEA_UNAUTH);
        }
    }


}