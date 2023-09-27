package com.bid.idearush.domain.idea.repository;

import com.bid.idearush.domain.idea.entity.Bid;
import com.bid.idearush.domain.idea.entity.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    Optional<Bid> findTopByIdeaOrderByBidPriceDesc(Idea idea);

    @Query("select distinct b.users.id from Bid b where b.idea.id = :ideaId")
    List<Long> findAllByIdeaDistinctUser(@Param("ideaId") Long ideaId);
}