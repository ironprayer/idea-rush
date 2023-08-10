package com.bid.idearush.domain.reservation.repository;

import com.bid.idearush.domain.reservation.model.entity.BidReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<BidReservation, Long> {

}
