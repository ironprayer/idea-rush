package com.bid.idearush.domain.idea.controller.reponse;

import com.bid.idearush.domain.idea.type.AuctionStatus;

public record IdeasResponse(
        Long id,
        String writer,
        String title,
        String content,
        String imageUrl,
        AuctionStatus status,
        Long minimumStartingPrice,
        Long BidWinPrice
) {}
