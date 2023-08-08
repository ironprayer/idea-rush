package com.bid.idearush.domain.idea.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.reponse.IdeaResponse;
import com.bid.idearush.domain.idea.model.request.IdeaRequest;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.idea.type.AuctionStatus;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.exception.IdeaFindException;
import com.bid.idearush.global.exception.IdeaWriteException;
import com.bid.idearush.global.exception.UserFindException;
import com.bid.idearush.global.exception.errortype.IdeaFindErrorCode;
import com.bid.idearush.global.exception.errortype.IdeaWriteErrorCode;
import com.bid.idearush.global.exception.errortype.UserFindErrorCode;
import com.bid.idearush.global.util.S3Service;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class IdeaServiceTest {

    @InjectMocks
    IdeaService ideaService;
    @Mock
    IdeaRepository ideaRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    S3Service s3Service;
    private Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
    private Category category = Category.LIFE;
    private String keyword = "expectedKeyword";

    @Nested
    @DisplayName("아이디어 전체조회 테스트")
    class IdeaFindAllTest {

        @Test
        @DisplayName("카테고리와 키워드를 에러를 발생시킨다.")
        void categoryAndKeywordFindThrowsExceptionFailTest() {
            IdeaFindException exception = assertThrows(IdeaFindException.class, () -> ideaService.findAllIdea(keyword, category));

            assertEquals(IdeaFindErrorCode.KEYWORD_CATEGORY_SAME.getStatus(), exception.getStatusCode());
            assertEquals(IdeaFindErrorCode.KEYWORD_CATEGORY_SAME.getMsg(), exception.getMessage());
        }

        @Test
        @DisplayName("카테고리별 리스트를 반환한다.")
        void categoryFindSuccessTest() {
            List<Idea> mockIdeaList = Collections.singletonList(createTestIdea());
            given(ideaRepository.findAllByCategory(category, sort)).willReturn(mockIdeaList);
            List<IdeaResponse> expectedIdeaResponseList = mockIdeaList.stream()
                    .map(IdeaResponse::from)
                    .collect(Collectors.toList());

            List<IdeaResponse> actualIdeaResponseList = ideaService.findAllIdea(null, category);

            assertThat(actualIdeaResponseList).hasSize(1);
            assertThat(actualIdeaResponseList).isEqualTo(expectedIdeaResponseList);
        }

        @Test
        @DisplayName("검색어를 통해 리스트를 반환한다.")
        void titleFindSuccessTest() {
            List<Idea> mockIdeaList = Collections.singletonList(createTestIdea());
            given(ideaRepository.findAllByTitleContaining(keyword, sort)).willReturn(mockIdeaList);
            List<IdeaResponse> expectedIdeaResponseList = mockIdeaList.stream()
                    .map(IdeaResponse::from)
                    .collect(Collectors.toList());

            List<IdeaResponse> actualIdeaResponseList = ideaService.findAllIdea(keyword, null);

            assertThat(actualIdeaResponseList).hasSize(1);
            assertThat(actualIdeaResponseList).isEqualTo(expectedIdeaResponseList);
        }

        @Test
        @DisplayName("일반 리스트를 반환한다.")
        void FindAllSuccessTest() {
            List<Idea> mockIdeaList = Collections.singletonList(createTestIdea());
            given(ideaRepository.findAll(sort)).willReturn(mockIdeaList);
            List<IdeaResponse> expectedIdeaResponseList = mockIdeaList.stream()
                    .map(IdeaResponse::from)
                    .collect(Collectors.toList());

            List<IdeaResponse> actualIdeaResponseList = ideaService.findAllIdea(null, null);

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

            assertThrows(IdeaFindException.class, () -> ideaService.findOneIdea(testIdeaId));
        }

        @Test
        @DisplayName("아이디어 상세 조회하는데 해당 아이디어를 반환한다.")
        void testFindOneSuccessTest() {
            Long testIdeaId = 1L;
            Idea expectedIdea = createTestIdea();
            given(ideaRepository.findById(testIdeaId)).willReturn(Optional.of(expectedIdea));

            IdeaResponse actualIdeaResponse = ideaService.findOneIdea(testIdeaId);

            assertThat(actualIdeaResponse).isEqualTo(IdeaResponse.from(expectedIdea));
        }

    }

    @Nested
    @DisplayName("아이디어 업데이트 테스트")
    class IdeaUpdateTest {

        IdeaRequest ideaRequest =
                new IdeaRequest("title", "content" , Category.values()[0], 1000L, LocalDateTime.now());
        MockMultipartFile multipartFile =
                new MockMultipartFile("name", "originalFilename", "image/jpeg", new byte[0]);

        @Test
        @DisplayName("업데이트 성공 케이스")
        void updateSuccessTest() {
            given(userRepository.findById(anyLong())).willReturn(Optional.of(Users.builder().build()));
            given(ideaRepository.findById(anyLong())).
                    willReturn(Optional.of(Idea.builder().id(1L).users(Users.builder().id(1L).build()).build()));

            assertDoesNotThrow(() -> ideaService.update(1L, 1L, ideaRequest, multipartFile));
        }

        @Test
        @DisplayName("유저가 존재하지 않아 실패하는 케이스")
        void updateNotUserFailTest() {
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> ideaService.update(1L, 1L, ideaRequest, multipartFile));

            assertEquals("유저가 존재하지 않습니다.", ex.getMessage());
        }

        @Test
        @DisplayName("아이디어가 존재하지 않아 실패하는 케이스")
        void updateNotIdeaFailTest() {
            given(userRepository.findById(anyLong())).willReturn(Optional.of(Users.builder().build()));
            given(ideaRepository.findById(anyLong())).
                    willThrow(new IllegalArgumentException("아이디어가 존재하지 않습니다."));

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () ->ideaService.update(1L, 1L, ideaRequest, multipartFile));

            assertEquals("아이디어가 존재하지 않습니다.", ex.getMessage());
        }

        @Test
        @DisplayName("아이디어 권한이 없어서 실패하는 케이스")
        void updateUnAuthIdeaFailTest() {
            given(userRepository.findById(anyLong())).willReturn(Optional.of(Users.builder().build()));
            given(ideaRepository.findById(anyLong())).
                    willReturn(Optional.of(Idea.builder().users(Users.builder().id(2L).build()).build()));

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () ->ideaService.update(1L, 1L, ideaRequest, multipartFile));

            assertEquals("아이디어에 권한이 없습니다.", ex.getMessage());
        }

    }

    @Nested
    @DisplayName("아이디어 삭제 테스트")
    class IdeaDeleteTest {

        @Test
        @DisplayName("삭제 성공 케이스")
        void deleteSuccessTest() {
            given(userRepository.findById(anyLong())).willReturn(Optional.of(Users.builder().build()));
            given(ideaRepository.findById(anyLong())).
                    willReturn(Optional.of(Idea.builder().users(Users.builder().id(1L).build()).build()));

            assertDoesNotThrow(() -> ideaService.deleteIdea(1L, 1L));
        }


        @Test
        @DisplayName("유저가 존재하지 않아 실패하는 케이스")
        void deleteNotUserFailTest() {
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());
            given(ideaRepository.findById(anyLong())).
                    willReturn(Optional.of(Idea.builder().users(Users.builder().id(2L).build()).build()));

            UserFindException ex = assertThrows(UserFindException.class,
                    () -> ideaService.deleteIdea(1L, 1L));

            assertEquals(UserFindErrorCode.USER_EMPTY.getStatus(), ex.getStatusCode());
            assertEquals(UserFindErrorCode.USER_EMPTY.getMsg(), ex.getMessage());
        }

        @Test
        @DisplayName("아이디어가 존재하지 않아 실패하는 케이스")
        void deleteNotIdeaFailTest() {
            given(ideaRepository.findById(anyLong())).
                    willThrow(new IdeaFindException(IdeaFindErrorCode.IDEA_EMPTY));

            IdeaFindException ex = assertThrows(IdeaFindException.class,
                    () -> ideaService.deleteIdea(1L, 1L));

            assertEquals(IdeaFindErrorCode.IDEA_EMPTY.getStatus(), ex.getStatusCode());
            assertEquals(IdeaFindErrorCode.IDEA_EMPTY.getMsg(), ex.getMessage());
        }

        @Test
        @DisplayName("아이디어 권한이 없어서 실패하는 케이스")
        void deleteUnAuthIdeaFailTest() {
            given(userRepository.findById(anyLong())).willReturn(Optional.of(Users.builder().build()));
            given(ideaRepository.findById(anyLong())).
                    willReturn(Optional.of(Idea.builder().users(Users.builder().id(2L).build()).build()));

            IdeaWriteException ex = assertThrows(IdeaWriteException.class,
                    () ->ideaService.deleteIdea(1L, 1L));

            assertEquals(IdeaWriteErrorCode.IDEA_UNAUTH.getStatus(), ex.getStatusCode());
            assertEquals(IdeaWriteErrorCode.IDEA_UNAUTH.getMsg(), ex.getMessage());
        }

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
                .users(Users.builder().build())
                .build();
    }
}