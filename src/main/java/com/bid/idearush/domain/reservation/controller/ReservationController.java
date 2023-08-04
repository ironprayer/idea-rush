package com.bid.idearush.domain.reservation.controller;

import com.bid.idearush.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/ideas")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    // 현재는 header에 토큰값이 없기 때문에 dummydata로 대체. 추후에 시큐리티로 추가됐을 때, 수정 필요
    // header에 토큰값이 담겼을 때 추가할 부분
    // 파라미터에 @AuthenticationPrincipal UserDetailsImpl userDetails 추가
    // ideaBidReservation 파라미터에 dummydata 지우고, userDetails.getUsername() 추가
    // ReservationService에 userId 지우고, String userNickname 추가
    // Repository에 findByUserNickname 추가 후, ReservationService에 findByUserNickname 으로 수정

    @PostMapping("/{id}/reservation")
    public ResponseEntity<String> ideaBidReservation(@PathVariable Long id) {
        Random random = new Random();
        Long dummyUserId = 1L + random.nextInt(20);
        reservationService.ideaBidReservation(id,dummyUserId);
        return new ResponseEntity<>("예약이 완료 되었습니다.", HttpStatus.OK);
    }
}
