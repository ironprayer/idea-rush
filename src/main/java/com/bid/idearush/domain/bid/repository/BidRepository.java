package com.bid.idearush.domain.bid.repository;

import com.bid.idearush.domain.bid.model.entity.Bid;
import com.bid.idearush.domain.idea.model.entity.Idea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {

    Optional<Bid> findTopByIdeaOrderByBidPriceDesc(Idea idea);

}