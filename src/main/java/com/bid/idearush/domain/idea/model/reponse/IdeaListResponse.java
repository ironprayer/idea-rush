package com.bid.idearush.domain.idea.model.reponse;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.type.AuctionStatus;
import com.bid.idearush.global.type.ServerIpAddress;

public record IdeaListResponse(
        Long id,
        String writer,
        String title,
        String content,
        String imageUrl,
        AuctionStatus status,
        Long minimumStartingPrice,
        Long BidWinPrice
) {


}
