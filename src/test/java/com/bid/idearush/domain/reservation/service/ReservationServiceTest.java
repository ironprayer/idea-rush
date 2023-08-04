package com.bid.idearush.domain.reservation.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.reservation.repository.ReservationRepository;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.exception.IdeaNotFoundException;
import com.bid.idearush.global.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    UserRepository userRepository;

    @Mock
    IdeaRepository ideaRepository;

    @Mock
    ReservationRepository reservationRepository;

    private Long userId = 1L;
    private Long ideaId = 1L;

    @Test
    @DisplayName("경매 예약")
    void testIdeaBidReservation_Success() {
        Users user = Users.builder().build();
        Idea idea = Idea.builder().build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(ideaRepository.findById(ideaId)).thenReturn(Optional.of(idea));

        reservationService.ideaBidReservation(ideaId,userId);

    }

    @Test
    @DisplayName("비회원 경매 예약")
    void testIdeaBidReservation_UserNotFound() {
        Idea idea = Idea.builder().build();
        when(ideaRepository.findById(ideaId)).thenReturn(Optional.of(idea));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> {
            reservationService.ideaBidReservation(ideaId, userId);
        });

        assertEquals("로그인이 필요합니다.", ex.getMessage());
    }

    @Test
    @DisplayName("삭제된 게시물 예약")
    void testIdeaBidReservation_IdeaNotFound() {
        when(ideaRepository.findById(ideaId)).thenReturn(Optional.empty());

        IdeaNotFoundException ex = assertThrows(IdeaNotFoundException.class, () -> {
            reservationService.ideaBidReservation(ideaId, userId);
        });

        assertEquals("게시글을 찾을 수 없습니다.", ex.getMessage());
    }
}