package com.bid.idearush.domain.idea.model.request;

import com.bid.idearush.domain.idea.type.Category;

import java.time.LocalDateTime;

public record IdeaRequest (
        String title,
        String content,
        Category category,
        Long minimumStartingPrice,
        LocalDateTime auctionStartTime
) {}
