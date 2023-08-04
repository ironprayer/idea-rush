package com.bid.idearush.domain.idea.model.entity;

import com.bid.idearush.domain.idea.model.request.IdeaRequest;
import com.bid.idearush.domain.idea.type.AuctionStatus;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.global.model.entity.BaseTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false)
    private LocalDateTime auctionStartTime;

    @Column(length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    Users users;

    public void updateOf(IdeaRequest ideaRequest, String imageName) {
        this.title = ideaRequest.title();
        this.content = ideaRequest.content();
        this.category = ideaRequest.category();
        this.imageName = imageName;
        this.auctionStartTime = ideaRequest.auctionStartTime();
        this.minimumStartingPrice = ideaRequest.minimumStartingPrice();
    }

}
