package com.bid.idearush.domain.idea.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.reponse.IdeaResponse;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.global.exception.IdeaFindException;
import com.bid.idearush.global.exception.errortype.IdeaFindErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j(topic = "아이디어 조회")
@Service
@RequiredArgsConstructor
public class IdeaFindService {

    private final IdeaRepository ideaRepository;

    @Transactional(readOnly = true)
    public IdeaResponse findOneIdea(Long ideaId) {

        Idea findIdea = ideaRepository.findById(ideaId)
                .orElseThrow(() -> {
                    throw new IdeaFindException(IdeaFindErrorCode.IDEA_EMPTY);
                });

        return IdeaResponse.from(findIdea);
    }

    @Transactional(readOnly = true)
    public List<IdeaResponse> findAllIdea(String keyword, Category category) {

        if (StringUtils.hasText(keyword) && !Objects.isNull(category)) {
            throw new IdeaFindException(IdeaFindErrorCode.KEYWORD_CATEGORY_SAME);
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");

        List<IdeaResponse> findList;

        if (StringUtils.hasText(keyword)) {
            findList = ideaRepository.findAllByTitleContaining(keyword, sort).stream()
                    .map(IdeaResponse::from)
                    .collect(Collectors.toList());
        }else if (!Objects.isNull(category)) {
            findList = ideaRepository.findAllByCategory(category, sort).stream()
                    .map(IdeaResponse::from)
                    .collect(Collectors.toList());
        }else {
            findList = ideaRepository.findAll(sort).stream()
                    .map(IdeaResponse::from)
                    .collect(Collectors.toList());
        }


        return findList;
    }


}
