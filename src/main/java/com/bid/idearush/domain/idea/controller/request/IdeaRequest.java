package com.bid.idearush.domain.idea.controller.request;

import com.bid.idearush.domain.idea.entity.Idea;
import com.bid.idearush.domain.idea.type.AuctionStatus;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.idea.type.DealStatus;
import com.bid.idearush.domain.user.entity.Users;

import java.time.LocalDateTime;

public record IdeaRequest (
        String title,
        String content,
        Category category,
        Long minimumStartingPrice,
        LocalDateTime auctionStartTime
) {

    public Idea toIdea(Users user, String imageName) {
        return Idea.builder()
                .category(category())
                .title(title())
                .content(content())
                .imageName(imageName)
                .minimumStartingPrice(minimumStartingPrice())
                .auctionStartTime(auctionStartTime())
                .auctionEndTime(auctionStartTime().plusMinutes(10))
                .auctionStatus(AuctionStatus.PREPARE)
                .dealStatus(DealStatus.NO_ACTION)
                .users(user)
                .build();
    }

}
