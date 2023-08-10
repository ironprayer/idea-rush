package com.bid.idearush.domain.reservation.repository;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.reservation.model.entity.BidReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<BidReservation, Long> {

    List<BidReservation> findAllByIdea(Idea idea);

}
