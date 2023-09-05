package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.reponse.IdeaListResponse;
import com.bid.idearush.domain.idea.model.reponse.IdeaOneResponse;
import com.bid.idearush.domain.idea.type.Category;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IdeaRepositoryCustom {

    Page<IdeaListResponse> findIdeaAll(Pageable pageable, long count);

    Page<IdeaListResponse> findCategory(Pageable pageable, Category category, long count);

    Page<IdeaListResponse> findTitle(Pageable pageable, String keyword);

    Optional<IdeaOneResponse> findIdeaOne(Long ideaId);


}
