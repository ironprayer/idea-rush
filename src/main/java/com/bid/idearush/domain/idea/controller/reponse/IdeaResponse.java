package com.bid.idearush.domain.idea.controller.reponse;

import com.bid.idearush.domain.idea.type.AuctionStatus;
import com.bid.idearush.domain.idea.type.Category;

import java.time.LocalDateTime;

public record IdeaResponse(
        Long id,
        String writer,
        String title,
        String content,
        String imageUrl,
        AuctionStatus status,
        Long minimumStartingPrice,
        Long BidWinPrice,
        Long BidLastPrice,
        Category category,
        LocalDateTime auctionStartTime
) {


}
