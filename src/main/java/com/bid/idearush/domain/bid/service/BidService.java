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
        Idea idea = ideaRepository.findByIdWithPessimisticLock(ideaId).orElseThrow(() ->
                new IdeaFindException(IdeaFindErrorCode.IDEA_EMPTY));

        Users user = userRepository.findById(userId).orElseThrow(() ->
                new UserFindException(UserFindErrorCode.USER_EMPTY));

        validateBidPrice(idea, request);

        Bid newBid = request.toBid(idea,user);

        bidRepository.save(newBid);
    }

    private void validateBidPrice(Idea idea, BidRequest request) {
        Bid currentBid = bidRepository.findTopByIdeaOrderByBidPriceDesc(idea)
                .orElse(null);

        Long requestBidPrice = request.bidPrice();
        Long startingBidPrice = idea.getMinimumStartingPrice();

        if (currentBid != null) {
            Long currentBidPrice =  currentBid.getBidPrice();
            if (requestBidPrice <= currentBidPrice || requestBidPrice > currentBidPrice * 1.1) {
                throw new BidWriteException(BidWriteErrorCode.INVALID_BID_PRICE);
            }
        } else if (requestBidPrice < startingBidPrice || requestBidPrice > startingBidPrice * 1.1) {
            throw new BidWriteException(BidWriteErrorCode.INVALID_BID_PRICE);
        }
    }

}
