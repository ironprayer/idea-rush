package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.idea.controller.reponse.IdeasResponse;
import com.bid.idearush.domain.idea.controller.reponse.IdeaResponse;
import com.bid.idearush.domain.idea.type.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IdeaRepositoryCustom {

    Page<IdeasResponse> findIdeaAll(Pageable pageable, long count);

    Page<IdeasResponse> findCategory(Pageable pageable, Category category, long count);

    Page<IdeasResponse> findTitle(Pageable pageable, String keyword);

    Optional<IdeaResponse> findIdeaOne(Long ideaId);


}
