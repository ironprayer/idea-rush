package com.bid.idearush.domain.idea.model.reponse;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.type.AuctionStatus;
import com.bid.idearush.global.type.ServerIpAddress;

public record IdeaResponse(
        String writer,
        String title,
        String content,
        String imageUrl,
        AuctionStatus status,
        Long minimumStartingPrice,
        Long bidWinPrice
) {

    public static IdeaResponse from(Idea idea){
        return new IdeaResponse(
                idea.getUsers().getNickname(),
                idea.getTitle(),
                idea.getContent(),
                ServerIpAddress.s3Address+idea.getImageName(),
                idea.getAuctionStatus(),
                idea.getMinimumStartingPrice(),
                idea.getBidWinPrice()
        );
    }
}
