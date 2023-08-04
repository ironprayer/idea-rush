package com.bid.idearush.domain.idea.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.reponse.IdeaResponse;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.global.exception.IdeaFindExceptionCustom;
import com.bid.idearush.global.exception.errortype.IdeaFindErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j(topic = "아이디어 조회")
@Service
@RequiredArgsConstructor
public class IdeaFindService {

    private final IdeaRepository ideaRepository;

    @Transactional(readOnly = true)
    public IdeaResponse findOne(Long ideaId) {

        Idea findIdea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> {
                    throw new IdeaFindExceptionCustom(IdeaFindErrorCode.IDEA_EMPTY);
                });

        return IdeaResponse.from(findIdea);
    }

    @Transactional(readOnly = true)
    public List<IdeaResponse> findAll(String keyword, String category) {

        if (StringUtils.hasText(keyword) && StringUtils.hasText(category)) {
            throw new IdeaFindExceptionCustom(IdeaFindErrorCode.KEYWORD_CATEGORY_SAME);
        }

        String sortColumn = "createdAt";
        Sort sort = Sort.by(Sort.Direction.ASC, sortColumn);

        List<IdeaResponse> findList = new ArrayList<>();

        if (StringUtils.hasText(keyword)) {
            findList = ideaRepository.findAllByTitleContaining(keyword, sort).stream()
                    .map(idea -> IdeaResponse.from(idea))
                    .collect(Collectors.toList());
        }

        if (StringUtils.hasText(category)) {
            findList = ideaRepository.findAllByCategory(parseStringToCategory(category), sort).stream()
                    .map(idea -> IdeaResponse.from(idea))
                    .collect(Collectors.toList());
        }

        if (!StringUtils.hasText(keyword) && !StringUtils.hasText(category)) {
            findList = ideaRepository.findAll(sort).stream()
                    .map(idea -> IdeaResponse.from(idea))
                    .collect(Collectors.toList());
        }


        return findList;
    }

    private Category parseStringToCategory(String category) {
        return switch (category) {
            case "삶" -> Category.LIFE;
            case "영화" -> Category.MOVIE;
            case "음악" -> Category.MUSIC;
            default -> null; //TODO: 예외 처리 해야 함.
        };
    }

}
