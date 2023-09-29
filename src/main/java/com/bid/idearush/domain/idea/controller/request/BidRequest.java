package com.bid.idearush.domain.idea.controller.request;

import com.bid.idearush.domain.idea.entity.Bid;
import com.bid.idearush.domain.idea.entity.Idea;
import com.bid.idearush.domain.user.entity.Users;

public record BidRequest (
        Long bidPrice
){

    public Bid toBid(Idea idea, Users user) {
        return Bid.builder()
                .idea(idea)
                .users(user)
                .bidPrice(bidPrice)
                .build();
    }

}
