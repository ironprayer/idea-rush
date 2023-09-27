package com.bid.idearush.domain.idea.controller;

import com.bid.idearush.domain.idea.service.ReservationService;
import com.bid.idearush.global.security.AuthPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ideas")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/{id}/reservation")
    public void ideaBidReservation(@PathVariable(name = "id") Long postId,
                                   @AuthenticationPrincipal AuthPayload authPayload) {
        reservationService.ideaBidReservation(postId, authPayload.userId());
    }

}
