package com.bid.idearush.domain.idea.service;

import com.bid.idearush.domain.idea.model.entity.Idea;
import com.bid.idearush.domain.idea.model.request.IdeaRequest;
import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.idea.type.Category;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.util.S3Service;
import org.assertj.core.internal.Bytes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IdeaServiceTest {

    @Nested
    @DisplayName("아이디어 업데이트 테스트")
    class IdeaUpdateTest {
        @Mock
        IdeaRepository ideaRepository;
        @Mock
        UserRepository userRepository;
        @Mock
        S3Service s3Service;
        IdeaRequest ideaRequest =
                new IdeaRequest("title", "content" , Category.values()[0], 1000L, LocalDateTime.now());
        MockMultipartFile multipartFile =
                new MockMultipartFile("name", "originalFilename", "contentType", new byte[0]);

        @Test
        @DisplayName("업데이트 성공 케이스")
        void updateSuccessTest() {
            IdeaService ideaService = new IdeaService(ideaRepository, userRepository, s3Service);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(Users.builder().build()));
            given(ideaRepository.findById(anyLong())).
                    willReturn(Optional.of(Idea.builder().users(Users.builder().id(1L).build()).build()));

            assertDoesNotThrow(() -> ideaService.update(1L, 1L, ideaRequest, multipartFile));
        }

        @Test
        @DisplayName("유저가 존재하지 않아 실패하는 케이스")
        void updateNotUserFailTest() {
            IdeaService ideaService = new IdeaService(ideaRepository, userRepository, s3Service);
            given(userRepository.findById(anyLong())).
                    willThrow(new IllegalArgumentException("유저가 존재하지 않습니다."));

            Exception ex = assertThrows(IllegalArgumentException.class,
                    () -> ideaService.update(1L, 1L, ideaRequest, multipartFile));

            assertEquals("유저가 존재하지 않습니다.", ex.getMessage());
        }

        @Test
        @DisplayName("아이디어가 존재하지 않아 실패하는 케이스")
        void updateNotIdeaFailTest() {
            IdeaService ideaService = new IdeaService(ideaRepository, userRepository, s3Service);
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
            IdeaService ideaService = new IdeaService(ideaRepository, userRepository, s3Service);
            given(userRepository.findById(anyLong())).willReturn(Optional.of(Users.builder().build()));
            given(ideaRepository.findById(anyLong())).
                    willReturn(Optional.of(Idea.builder().users(Users.builder().id(2L).build()).build()));

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () ->ideaService.update(1L, 1L, ideaRequest, multipartFile));

            assertEquals("아이디어에 권한이 없습니다.", ex.getMessage());
        }
    }
}