package com.bid.idearush.domain.idea.model.reponse;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.type.AuctionStatus;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.global.type.ServerIpAddress;

import java.time.LocalDateTime;

public record IdeaOneResponse(
        String writer,
        String title,
        String content,
        String imageUrl,
        AuctionStatus status,
        Long minimumStartingPrice,
        Long BidWinPrice,
        Category category,
        LocalDateTime auctionStartTime
) {
    public static IdeaOneResponse from(Idea idea){
        return new IdeaOneResponse(
                idea.getUsers().getNickname(),
                idea.getTitle(),
                idea.getContent(),
                ServerIpAddress.s3Address+idea.getImageName(),
                idea.getAuctionStatus(),
                idea.getMinimumStartingPrice(),
                idea.getBidWinPrice(),
                idea.getCategory(),
                idea.getAuctionStartTime()
        );
    }

}
