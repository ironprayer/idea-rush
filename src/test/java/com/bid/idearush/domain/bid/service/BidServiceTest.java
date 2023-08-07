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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class BidServiceTest {

    @InjectMocks
    private BidService bidService;

    @Mock
    UserRepository userRepository;

    @Mock
    IdeaRepository ideaRepository;

    @Mock
    BidRepository bidRepository;

    private Long userId = 1L;
    private Long ideaId = 1L;
    private BidRequest bidRequest = new BidRequest(1000L);

    @Nested
    @DisplayName("입찰 테스트")
    class BidTest {

        @Test
        @DisplayName("입찰 성공")
        void BidSuccessTest() {
            given(userRepository.findById(userId)).willReturn(Optional.of(Users.builder().build()));
            given(ideaRepository.findByIdWithPessimisticLock(ideaId)).willReturn(Optional.of(Idea.builder().minimumStartingPrice(950L).build()));

            bidService.createBid(ideaId, userId, bidRequest);
        }

        @Test
        @DisplayName("예외 상황: 삭제된 경매 게시글에 입찰할 경우")
        void bidIdeaNotFoundFailTest() {
            given(ideaRepository.findByIdWithPessimisticLock(ideaId)).willReturn(Optional.empty());

            IdeaFindException ex = assertThrows(IdeaFindException.class, () -> {
                bidService.createBid(ideaId, userId, bidRequest);
            });

            assertEquals(IdeaFindErrorCode.IDEA_EMPTY.getStatus(), ex.getStatusCode());
            assertEquals(IdeaFindErrorCode.IDEA_EMPTY.getMsg(), ex.getMessage());
        }

        @Test
        @DisplayName("예외 상황: 비회원이 입찰할 경우")
        void bidUserNotFoundFailTest() {
            given(ideaRepository.findByIdWithPessimisticLock(ideaId)).willReturn(Optional.of(Idea.builder().build()));
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            UserFindException ex = assertThrows(UserFindException.class, () -> {
                bidService.createBid(ideaId, userId, bidRequest);
            });

            assertEquals(UserFindErrorCode.USER_EMPTY.getStatus(), ex.getStatusCode());
            assertEquals(UserFindErrorCode.USER_EMPTY.getMsg(), ex.getMessage());
        }

        @Test
        @DisplayName("예외 상황: 현재 입찰가보다 낮게 입찰할 경우")
        void bidPriceLowerThanCurrentPriceFailTest() {
            given(userRepository.findById(userId)).willReturn(Optional.of(Users.builder().build()));
            given(ideaRepository.findByIdWithPessimisticLock(ideaId)).willReturn(Optional.of(Idea.builder().minimumStartingPrice(1200L).build()));
            given(bidRepository.findTopByIdeaOrderByBidPriceDesc(any(Idea.class)))
                    .willReturn(Optional.of(Bid.builder().bidPrice(800L).build()));

            BidWriteException ex = assertThrows(BidWriteException.class, () -> {
                bidService.createBid(ideaId, userId, bidRequest);
            });

            assertEquals(BidWriteErrorCode.INVALID_BID_PRICE.getStatus(), ex.getStatusCode());
            assertEquals(BidWriteErrorCode.INVALID_BID_PRICE.getMsg(), ex.getMessage());
        }

        @Test
        @DisplayName("예외 상황: 현재 입찰가보다 10% 초과하여 입찰할 경우")
        void bidPriceOverFailTest() {
            given(userRepository.findById(userId)).willReturn(Optional.of(Users.builder().build()));
            given(ideaRepository.findByIdWithPessimisticLock(ideaId)).willReturn(Optional.of(Idea.builder().minimumStartingPrice(800L).build()));
            given(bidRepository.findTopByIdeaOrderByBidPriceDesc(any(Idea.class)))
                    .willReturn(Optional.of(Bid.builder().bidPrice(800L).build()));

            BidWriteException ex = assertThrows(BidWriteException.class, () -> {
                bidService.createBid(ideaId, userId, bidRequest);
            });

            assertEquals(BidWriteErrorCode.INVALID_BID_PRICE.getStatus(), ex.getStatusCode());
            assertEquals(BidWriteErrorCode.INVALID_BID_PRICE.getMsg(), ex.getMessage());
        }

        @Test
        @DisplayName("예외 상황: 경매 시작가보다 낮게 입찰할 경우")
        void bidPriceLowerThanStartingPriceFailTest() {
            given(userRepository.findById(userId)).willReturn(Optional.of(Users.builder().build()));
            given(ideaRepository.findByIdWithPessimisticLock(ideaId)).willReturn(Optional.of(Idea.builder().minimumStartingPrice(1200L).build()));
            given(bidRepository.findTopByIdeaOrderByBidPriceDesc(any(Idea.class)))
                    .willReturn(Optional.of(Bid.builder().bidPrice(800L).build()));

            BidWriteException ex = assertThrows(BidWriteException.class, () -> {
                bidService.createBid(ideaId, userId, bidRequest);
            });

            assertEquals(BidWriteErrorCode.INVALID_BID_PRICE.getStatus(), ex.getStatusCode());
            assertEquals(BidWriteErrorCode.INVALID_BID_PRICE.getMsg(), ex.getMessage());
        }

        @Test
        @DisplayName("예외 상황: 경매 시작가보다 10% 초과하여 입찰할 경우")
        void bidPriceOverStartingPriceFailTest() {
            given(userRepository.findById(userId)).willReturn(Optional.of(Users.builder().build()));
            given(ideaRepository.findByIdWithPessimisticLock(ideaId)).willReturn(Optional.of(Idea.builder().minimumStartingPrice(800L).build()));
            given(bidRepository.findTopByIdeaOrderByBidPriceDesc(any(Idea.class)))
                    .willReturn(Optional.of(Bid.builder().bidPrice(900L).build()));

            BidWriteException ex = assertThrows(BidWriteException.class, () -> {
                bidService.createBid(ideaId, userId, bidRequest);
            });

            assertEquals(BidWriteErrorCode.INVALID_BID_PRICE.getStatus(), ex.getStatusCode());
            assertEquals(BidWriteErrorCode.INVALID_BID_PRICE.getMsg(), ex.getMessage());
        }

    }

}