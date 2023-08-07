package com.bid.idearush.domain.reservation.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.reservation.model.entity.BidReservation;
import com.bid.idearush.domain.reservation.repository.ReservationRepository;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;

    public void ideaBidReservation(Long ideaId, Long userId) {
        Idea idea = ideaRepository.findById(ideaId).orElseThrow(() ->
                new NoSuchElementException("게시글을 찾을 수 없습니다."));
        Users user = userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException("로그인이 필요합니다."));

        BidReservation bidReservation = BidReservation.builder()
                                            .users(user)
                                            .idea(idea)
                                            .build();
        reservationRepository.save(bidReservation);
    }

}
