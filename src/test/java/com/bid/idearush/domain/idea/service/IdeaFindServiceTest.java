package com.bid.idearush.domain.idea.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.reponse.IdeaResponse;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.idea.type.AuctionStatus;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.global.exception.IdeaFindExceptionCustom;
import com.bid.idearush.global.exception.errortype.IdeaFindErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class IdeaFindServiceTest {

    @Mock
    private IdeaRepository ideaRepository;


    private Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");

    @Test
    @DisplayName("카테고리와 키워드를 에러를 발생시킨다.")
    void categoryAndKeywordFindThrowsException() {
        String category = "expectedCategory";
        String keyword = "expectedKeyword";

        IdeaFindExceptionCustom exception = assertThrows(IdeaFindExceptionCustom.class, () -> ideaFindService().findAll(keyword, category));
        assertEquals(IdeaFindErrorCode.KEYWORD_CATEGORY_SAME.getStatus(), exception.getHttpStatus());
        assertEquals(IdeaFindErrorCode.KEYWORD_CATEGORY_SAME.getMsg(), exception.getMsg());
    }

    @Test
    void categoryFindTest() {
        String categoryParamString = "삶";
        Category expectedCategory = Category.LIFE;
        Idea testIdea = createCategoryTestIdea(expectedCategory);
        List<Idea> mockIdeaList = Collections.singletonList(testIdea);
        given(ideaRepository.findAllByCategory(expectedCategory, sort)).willReturn(mockIdeaList);
        List<IdeaResponse> expectedIdeaResponseList = mockIdeaList.stream()
                .map(IdeaResponse::from)
                .collect(Collectors.toList());

        List<IdeaResponse> actualIdeaResponseList = ideaFindService().findAll(null, categoryParamString);

        assertThat(actualIdeaResponseList).hasSize(1);
        assertThat(actualIdeaResponseList).isEqualTo(expectedIdeaResponseList);
    }

    @Test
    void TitleFindTest() {
        String keyword = "expectedCategory";
        Idea testIdea = createTitleTestIdea(keyword);
        List<Idea> mockIdeaList = Collections.singletonList(testIdea);
        given(ideaRepository.findAllByTitleContaining(keyword, sort)).willReturn(mockIdeaList);
        List<IdeaResponse> expectedIdeaResponseList = mockIdeaList.stream()
                .map(IdeaResponse::from)
                .collect(Collectors.toList());

        List<IdeaResponse> actualIdeaResponseList = ideaFindService().findAll(keyword, null);

        assertThat(actualIdeaResponseList).hasSize(1);
        assertThat(actualIdeaResponseList).isEqualTo(expectedIdeaResponseList);
    }

    @Test
    void testFindOneThrowsException() {
        Long testIdeaId = 1L;
        given(ideaRepository.findById(testIdeaId)).willReturn(Optional.empty());

        assertThrows(IdeaFindExceptionCustom.class, () -> ideaFindService().findOne(testIdeaId));
    }

    @Test
    void testFindOne() {
        Long testIdeaId = 1L;
        Idea expectedIdea = createTestIdea();
        given(ideaRepository.findById(testIdeaId)).willReturn(Optional.of(expectedIdea));

        IdeaResponse actualIdeaResponse = ideaFindService().findOne(testIdeaId);

        assertThat(actualIdeaResponse).isEqualTo(IdeaResponse.from(expectedIdea));
    }

    private IdeaFindService ideaFindService() {
        return new IdeaFindService(ideaRepository);
    }

    private Idea createTitleTestIdea(String title) {

        return Idea.builder()
                .category(Category.LIFE)
                .title(title)
                .content("content")
                .imageName("imageName")
                .minimumStartingPrice(0L)
                .auctionStartTime(LocalDateTime.now())
                .auctionStatus(AuctionStatus.PREPARE)
                .users(new Users())
                .build();
    }

    private Idea createCategoryTestIdea(Category category) {

        return Idea.builder()
                .category(category)
                .title("title")
                .content("content")
                .imageName("imageName")
                .minimumStartingPrice(0L)
                .auctionStartTime(LocalDateTime.now())
                .auctionStatus(AuctionStatus.PREPARE)
                .users(new Users())
                .build();
    }

    private Idea createTestIdea(){

        return Idea.builder()
                .category(Category.LIFE)
                .title("title")
                .content("content")
                .imageName("imageName")
                .minimumStartingPrice(0L)
                .auctionStartTime(LocalDateTime.now())
                .auctionStatus(AuctionStatus.PREPARE)
                .users(new Users())
                .build();
    }


}
