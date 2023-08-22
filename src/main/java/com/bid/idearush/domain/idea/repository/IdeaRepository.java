package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.type.Category;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IdeaRepository extends
        JpaRepository<Idea, Long>,
        IdeaRepositoryCustom,
        QuerydslPredicateExecutor {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Idea m WHERE m.id = :id")
    Optional<Idea> findByIdWithPessimisticLock(@Param("id") Long id);

    @Query("SELECT m FROM Idea m WHERE m.auctionStatus = 'ONGOING' and  m.auctionEndTime <= current_timestamp")
    List<Idea> findEndIdeas();

    @Query("SELECT m FROM Idea m WHERE m.auctionStatus = 'PREPARE' and :minTime <= m.auctionStartTime and  m.auctionStartTime <= :maxTime")
    List<Idea> findBeforeTime(LocalDateTime minTime, LocalDateTime maxTime);
}