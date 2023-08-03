package com.bid.idearush.domain.bid.model.entity;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.global.model.entity.BaseTime;
import jakarta.persistence.*;

@Entity
public class BidFail extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idea_id")
    private Idea idea;
}
