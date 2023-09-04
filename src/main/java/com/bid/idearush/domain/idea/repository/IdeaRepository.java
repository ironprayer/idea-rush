package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.idea.model.entity.Idea;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
    List<Idea> findBeforeTime(@Param("minTime") LocalDateTime minTime, @Param("maxTime") LocalDateTime maxTime);

    @Modifying
    @Transactional
    @Query("UPDATE Idea m SET m.auctionStatus = 'ONGOING' WHERE m.auctionStatus = 'PREPARE' and m.auctionStartTime <= :currentTime")
    void updatePrepareToOngoing(@Param("currentTime") LocalDateTime currentTime);

}