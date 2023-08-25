package com.bid.idearush.domain.bid.service;

import com.bid.idearush.domain.bid.model.entity.Bid;
import com.bid.idearush.domain.bid.model.request.BidRequest;
import com.bid.idearush.domain.bid.repository.BidRepository;
import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.sse.service.SseService;
import com.bid.idearush.domain.sse.type.SseConnect;
import com.bid.idearush.domain.sse.type.SseEvent;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.exception.BidWriteException;
import com.bid.idearush.global.exception.IdeaFindException;
import com.bid.idearush.global.exception.UserFindException;
import com.bid.idearush.global.exception.errortype.BidWriteErrorCode;
import com.bid.idearush.global.exception.errortype.IdeaFindErrorCode;
import com.bid.idearush.global.exception.errortype.UserFindErrorCode;
import com.bid.idearush.global.util.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final NoticeService noticeService;
    private final SseService sseService;

    @Transactional
    public void createBid(Long ideaId, Long userId, BidRequest request) {
        long point_one = System.currentTimeMillis();
        Idea idea = ideaRepository.findByIdWithPessimisticLock(ideaId).orElseThrow(() ->
                new IdeaFindException(IdeaFindErrorCode.IDEA_EMPTY));
        long point_two = System.currentTimeMillis();
        Users user = userRepository.findById(userId).orElseThrow(() ->
                new UserFindException(UserFindErrorCode.USER_EMPTY));
        long point_three = System.currentTimeMillis();

        validateBidPrice(idea, request);
        long point_four = System.currentTimeMillis();
        Bid newBid = request.toBid(idea,user);

        bidRepository.save(newBid);
        long point_five = System.currentTimeMillis();
        sseService.send(SseConnect.BID,SseEvent.BID_PRICE_UPDATE,ideaId,newBid.getBidPrice());
        long point_six = System.currentTimeMillis();
        noticeService.noticeBidEvent(userId, idea, request.bidPrice());
        long point_seven = System.currentTimeMillis();

        log.info( "Idea Id : " + ideaId
                + " Bid Id : " + newBid.getId()
                + " Start Time :" + point_one
                + " All Time : " + (point_seven - point_one)
                + " Idea Find Time : " + (point_two - point_one)
                + " User Find Time : " + (point_three - point_two)
                + " Validate Time : " + (point_four - point_three)
                + " Bid Save : " + (point_five - point_four)
                + " Bid Update Send : " + (point_six - point_five)
                + " Bid Notice Send : " + (point_seven - point_six));
    }

    private void validateBidPrice(Idea idea, BidRequest request) {
        Bid currentBid = bidRepository.findTopByIdeaOrderByBidPriceDesc(idea)
                .orElse(null);

        Long requestBidPrice = request.bidPrice();
        Long minBidPrice = currentBid == null ? idea.getMinimumStartingPrice() : currentBid.getBidPrice();
        Long maxBidPrice = (long) (minBidPrice * 1.1);

        if (requestBidPrice <= minBidPrice || requestBidPrice > maxBidPrice) {
            throw new BidWriteException(BidWriteErrorCode.INVALID_BID_PRICE);
        }
    }

}
