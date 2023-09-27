package com.bid.idearush.domain.bid.model.request;

import com.bid.idearush.domain.bid.model.entity.Bid;
import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.user.model.entity.Users;

public record BidRequest (
        Long bidPrice
){

    public Bid toBid(Idea idea, Users user) {
        return Bid.builder()
                .idea(idea)
                .users(user)
                .bidPrice(this.bidPrice())
                .build();
    }

}
