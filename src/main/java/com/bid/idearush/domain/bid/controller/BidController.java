package com.bid.idearush.domain.bid.controller;

import com.bid.idearush.domain.bid.model.request.BidRequest;
import com.bid.idearush.domain.bid.service.BidService;
import com.bid.idearush.global.security.AuthPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("api/ideas/{id}/bid")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    public void createBid(@PathVariable(name = "id") Long ideaId,
                          @RequestBody BidRequest request,
                          @AuthenticationPrincipal AuthPayload authPayload) {

        bidService.createBid(ideaId, authPayload.userId(), request);
    }

}
