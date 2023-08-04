package com.bid.idearush.domain.bid.model.entity;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.global.model.entity.BaseTime;
import jakarta.persistence.*;

@Entity
public class Bid extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bidPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idea_id")
    private Idea idea;
}
