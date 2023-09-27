package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.idea.controller.reponse.IdeaListResponse;
import com.bid.idearush.domain.idea.controller.reponse.IdeaOneResponse;
import com.bid.idearush.domain.idea.type.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IdeaRepositoryCustom {

    Page<IdeaListResponse> findIdeaAll(Pageable pageable, long count);

    Page<IdeaListResponse> findCategory(Pageable pageable, Category category, long count);

    Page<IdeaListResponse> findTitle(Pageable pageable, String keyword);

    Optional<IdeaOneResponse> findIdeaOne(Long ideaId);


}
