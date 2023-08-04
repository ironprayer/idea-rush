package com.bid.idearush.domain.reservation.model.entity;

import com.bid.idearush.domain.idea.model.Idea;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.global.model.entity.BaseTime;
import jakarta.persistence.*;

@Entity
public class BidReservation extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idea_id")
    private Idea idea;
}
