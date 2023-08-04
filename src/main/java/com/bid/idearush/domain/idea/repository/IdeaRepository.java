package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.idea.model.entity.Idea;

import com.bid.idearush.domain.idea.type.Category;
import jakarta.persistence.Id;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IdeaRepository extends JpaRepository<Idea, Long> {

    List<Idea> findAllByCategory(Category category, Sort sort);
    List<Idea> findAllByTitleContaining(String keyword, Sort sort);

}
