package com.bid.idearush.domain.idea.entity;

import com.bid.idearush.domain.idea.controller.request.IdeaRequest;
import com.bid.idearush.domain.idea.type.AuctionStatus;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.idea.type.DealStatus;
import com.bid.idearush.domain.user.entity.Users;
import com.bid.idearush.global.model.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Idea extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(length = 64, nullable = false)
    private String title;

    @Column(length = 1024, nullable = false)
    private String content;

    @Column(length = 64)
    private String imageName;

    @Column(nullable = false)
    private Long minimumStartingPrice;

    @Column
    private Long bidWinPrice;

    @Column(nullable = false)
    private LocalDateTime auctionStartTime;

    @Column(nullable = false)
    private LocalDateTime auctionEndTime;

    @Column(length = 8, nullable = false)
    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;

    @Column(length = 16)
    @Enumerated(EnumType.STRING)
    private DealStatus dealStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public void updateOf(IdeaRequest ideaRequest, String imageName) {
        this.title = ideaRequest.title();
        this.content = ideaRequest.content();
        this.category = ideaRequest.category();
        this.imageName = imageName;
        this.auctionStartTime = ideaRequest.auctionStartTime();
        this.auctionEndTime = ideaRequest.auctionStartTime().plusMinutes(10);
        this.minimumStartingPrice = ideaRequest.minimumStartingPrice();
    }

    public void updateBidSuccess(Long bidWinPrice){
        this.auctionStatus = AuctionStatus.END;
        this.dealStatus = DealStatus.BID_WIN;
        this.bidWinPrice = bidWinPrice;
    }

    public void updateBidFail(){
        this.auctionStatus = AuctionStatus.END;
        this.dealStatus = DealStatus.BID_FAIL;
    }

    public boolean isAuthUser(Long userId) {
        return userId.equals(this.users.getId());
    }

    public void changeImage(String imagePath) {
        this.imageName = imagePath;
    }
}