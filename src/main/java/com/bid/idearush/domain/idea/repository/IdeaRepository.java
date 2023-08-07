package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.type.Category;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IdeaRepository extends JpaRepository<Idea, Long> {

    List<Idea> findAllByCategory(Category category, Sort sort);
    List<Idea> findAllByTitleContaining(String keyword, Sort sort);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Idea m WHERE m.id = :id")
    Optional<Idea> findByIdWithPessimisticLock(@Param("id") Long id);

}