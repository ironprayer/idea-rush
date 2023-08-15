package com.bid.idearush.domain.idea.model.request;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.type.AuctionStatus;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.idea.type.DealStatus;
import com.bid.idearush.domain.user.model.entity.Users;

import java.time.LocalDateTime;

public record IdeaRequest (
        String title,
        String content,
        Category category,
        Long minimumStartingPrice,
        LocalDateTime auctionStartTime
) {

    public Idea toIdea(Users user, String imageName) {
        LocalDateTime auctionEndTime = auctionStartTime().plusMinutes(10);

        Idea idea = Idea.builder()
                .category(category())
                .title(title())
                .content(content())
                .imageName(imageName)
                .minimumStartingPrice(minimumStartingPrice())
                .auctionStartTime(auctionStartTime())
                .auctionEndTime(auctionEndTime)
                .auctionStatus(AuctionStatus.PREPARE)
                .dealStatus(DealStatus.NO_ACTION)
                .users(user)
                .build();
        return idea;
    }

}
