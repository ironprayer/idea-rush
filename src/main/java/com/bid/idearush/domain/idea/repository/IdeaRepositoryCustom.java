package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.idea.model.reponse.IdeaResponse;
import com.bid.idearush.domain.idea.type.Category;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IdeaRepositoryCustom {

    List<IdeaResponse> findIdeaAll(Pageable pageable);
    List<IdeaResponse> findCategoryAndTitleAll(Category category, String keyword, Pageable pageable);
    Optional<IdeaResponse> findIdeaOne(Long ideaId);

}
