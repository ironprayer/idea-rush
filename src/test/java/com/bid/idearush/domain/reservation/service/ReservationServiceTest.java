package com.bid.idearush.domain.reservation.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.reservation.repository.ReservationRepository;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

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

    @Nested
    @DisplayName("경매 예약")
    class IdeaBidReservation {
        @Test
        @DisplayName("경매 예약 성공")
        void ideaBidReservationSuccessTest() {
            Users user = Users.builder().build();
            Idea idea = Idea.builder().build();
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(ideaRepository.findById(ideaId)).willReturn(Optional.of(idea));

            reservationService.ideaBidReservation(ideaId, userId);
        }

        @Test
        @DisplayName("예외 상황: 비회원이 경매를 예약할 경우")
        void ideaBidReservationUserNotFoundFailTest() {
            given(ideaRepository.findById(ideaId)).willReturn(Optional.of(Idea.builder().build()));
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> {
                reservationService.ideaBidReservation(ideaId, userId);
            });

            assertEquals("로그인이 필요합니다.", ex.getMessage());
        }

        @Test
        @DisplayName("예외 상황: 삭제된 경매 게시글을 예약할 경우")
        void ideaBidReservationIdeaNotFoundFailTest() {
            given(ideaRepository.findById(ideaId)).willReturn(Optional.empty());

            IdeaNotFoundException ex = assertThrows(IdeaNotFoundException.class, () -> {
                reservationService.ideaBidReservation(ideaId, userId);
            });

            assertEquals("게시글을 찾을 수 없습니다.", ex.getMessage());
        }
    }

}