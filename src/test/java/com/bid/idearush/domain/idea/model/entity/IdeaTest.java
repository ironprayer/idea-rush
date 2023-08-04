package com.bid.idearush.domain.idea.model.entity;

import com.bid.idearush.domain.idea.model.request.IdeaRequest;
import com.bid.idearush.domain.idea.type.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class IdeaTest {

    @Test
    @DisplayName("IdeaRequest 정보를 사용해 아디이어 업데이트 테스트 ")
    void updateOfIdeaRequestTest() {
        IdeaRequest ideaRequest =
                new IdeaRequest("title", "content" , Category.values()[0], 1000L, LocalDateTime.now());
        String imageName = "imageName";
        Idea idea = Idea.builder().build();

        idea.updateOf(ideaRequest, imageName);

        assertEquals(ideaRequest.title(), idea.getTitle());
        assertEquals(ideaRequest.category(), idea.getCategory());
        assertEquals(ideaRequest.content(), idea.getContent());
        assertEquals(ideaRequest.minimumStartingPrice(), idea.getMinimumStartingPrice());
        assertEquals(ideaRequest.auctionStartTime(), idea.getAuctionStartTime());
        assertEquals(imageName, idea.getImageName());
    }
}