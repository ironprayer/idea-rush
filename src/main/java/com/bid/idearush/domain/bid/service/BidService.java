package com.bid.idearush.domain.bid.service;

import com.bid.idearush.domain.bid.model.entity.Bid;
import com.bid.idearush.domain.bid.model.request.BidRequest;
import com.bid.idearush.domain.bid.repository.BidRepository;
import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.exception.BidWriteException;
import com.bid.idearush.global.exception.IdeaFindException;
import com.bid.idearush.global.exception.UserFindException;
import com.bid.idearush.global.exception.errortype.BidWriteErrorCode;
import com.bid.idearush.global.exception.errortype.IdeaFindErrorCode;
import com.bid.idearush.global.exception.errortype.UserFindErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BidService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;

    @Transactional
    public void createBid(Long ideaId, Long userId, BidRequest request) {
        Idea idea = ideaRepository.findById(ideaId).orElseThrow(() ->
                new IdeaFindException(IdeaFindErrorCode.IDEA_EMPTY));

        Users user = userRepository.findById(userId).orElseThrow(() ->
                new UserFindException(UserFindErrorCode.USER_EMPTY));

        bidPriceValidation(idea, request);

        Bid newBid = Bid.builder()
                .idea(idea)
                .users(user)
                .bidPrice(request.bidPrice())
                .build();

        bidRepository.save(newBid);
    }

    private void bidPriceValidation(Idea idea, BidRequest request) {
        Bid currentBidPrice = bidRepository.findTopByIdeaOrderByBidPriceDesc(idea)
                .orElse(null);

        if (currentBidPrice != null) {
            if (request.bidPrice() <= currentBidPrice.getBidPrice()) {
                throw new BidWriteException(BidWriteErrorCode.BID_PRICE_LOWER_THAN_CURRENT_PRICE);
            } else if (request.bidPrice() > currentBidPrice.getBidPrice() * 1.1) {
                throw new BidWriteException(BidWriteErrorCode.BID_PRICE_OVER_THAN_CURRENT_PRICE);
            }
        } else if (request.bidPrice() < idea.getMinimumStartingPrice()) {
            throw new BidWriteException(BidWriteErrorCode.BID_PRICE_LOWER_THAN_STARTING_PRICE);
        } else if (request.bidPrice() > idea.getMinimumStartingPrice() * 1.1) {
            throw new BidWriteException(BidWriteErrorCode.BID_PRICE_OVER_THAN_STARTING_PRICE);
        }
    }

}
