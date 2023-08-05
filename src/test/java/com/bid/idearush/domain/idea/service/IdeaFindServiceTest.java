package com.bid.idearush.domain.idea.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.reponse.IdeaResponse;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.idea.type.AuctionStatus;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.global.exception.IdeaFindException;
import com.bid.idearush.global.exception.errortype.IdeaFindErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
    @InjectMocks
    private IdeaFindService ideaFindService;
    private Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
    private Category category = Category.LIFE;
    private String keyword = "expectedKeyword";

    @Nested
    @DisplayName("아이디어 전체조회 테스트")
    class IdeaFindAllTest {

        @Test
        @DisplayName("카테고리와 키워드를 에러를 발생시킨다.")
        void categoryAndKeywordFindThrowsExceptionFailTest() {
            IdeaFindException exception = assertThrows(IdeaFindException.class, () -> ideaFindService.findAllIdea(keyword, category));

            assertEquals(IdeaFindErrorCode.KEYWORD_CATEGORY_SAME.getStatus(), exception.getStatusCode());
            assertEquals(IdeaFindErrorCode.KEYWORD_CATEGORY_SAME.getMsg(), exception.getMessage());
        }

        @Test
        @DisplayName("카테고리별 리스트를 반환한다.")
        void categoryFindSuccessTest() {
            List<Idea> mockIdeaList = Collections.singletonList(createTitleTestIdea());
            given(ideaRepository.findAllByCategory(category, sort)).willReturn(mockIdeaList);
            List<IdeaResponse> expectedIdeaResponseList = mockIdeaList.stream()
                    .map(IdeaResponse::from)
                    .collect(Collectors.toList());

            List<IdeaResponse> actualIdeaResponseList = ideaFindService.findAllIdea(null, category);

            assertThat(actualIdeaResponseList).hasSize(1);
            assertThat(actualIdeaResponseList).isEqualTo(expectedIdeaResponseList);
        }

        @Test
        @DisplayName("검색어를 통해 리스트를 반환한다.")
        void titleFindSuccessTest() {
            List<Idea> mockIdeaList = Collections.singletonList(createTitleTestIdea());
            given(ideaRepository.findAllByTitleContaining(keyword, sort)).willReturn(mockIdeaList);
            List<IdeaResponse> expectedIdeaResponseList = mockIdeaList.stream()
                    .map(IdeaResponse::from)
                    .collect(Collectors.toList());

            List<IdeaResponse> actualIdeaResponseList = ideaFindService.findAllIdea(keyword, null);

            assertThat(actualIdeaResponseList).hasSize(1);
            assertThat(actualIdeaResponseList).isEqualTo(expectedIdeaResponseList);
        }

        @Test
        @DisplayName("일반 리스트를 반환한다.")
        void FindAllSuccessTest() {
            List<Idea> mockIdeaList = Collections.singletonList(createTitleTestIdea());
            given(ideaRepository.findAll(sort)).willReturn(mockIdeaList);
            List<IdeaResponse> expectedIdeaResponseList = mockIdeaList.stream()
                    .map(IdeaResponse::from)
                    .collect(Collectors.toList());

            List<IdeaResponse> actualIdeaResponseList = ideaFindService.findAllIdea(null, null);

            assertThat(actualIdeaResponseList).hasSize(1);
            assertThat(actualIdeaResponseList).isEqualTo(expectedIdeaResponseList);
        }
    }

    @Nested
    @DisplayName("아이디어 상세 조회 테스트")
    class IdeaFindOneTest {

        @Test
        @DisplayName("아이디어 상세 조회하는데 해당 아이디어가 없을 경우")
        void testFindOneThrowsExceptionFailTest() {
            Long testIdeaId = 1L;
            given(ideaRepository.findById(testIdeaId)).willReturn(Optional.empty());

            assertThrows(IdeaFindException.class, () -> ideaFindService.findOneIdea(testIdeaId));
        }

        @Test
        @DisplayName("아이디어 상세 조회하는데 해당 아이디어를 반환한다.")
        void testFindOneSuccessTest() {
            Long testIdeaId = 1L;
            Idea expectedIdea = createTitleTestIdea();
            given(ideaRepository.findById(testIdeaId)).willReturn(Optional.of(expectedIdea));

            IdeaResponse actualIdeaResponse = ideaFindService.findOneIdea(testIdeaId);

            assertThat(actualIdeaResponse).isEqualTo(IdeaResponse.from(expectedIdea));
        }

    }

    private Idea createTitleTestIdea() {
        return Idea.builder()
                .category(category)
                .title(keyword)
                .content("content")
                .imageName("imageName")
                .minimumStartingPrice(0L)
                .auctionStartTime(LocalDateTime.now())
                .auctionStatus(AuctionStatus.PREPARE)
                .users(Users.builder().build())
                .build();
    }


}
