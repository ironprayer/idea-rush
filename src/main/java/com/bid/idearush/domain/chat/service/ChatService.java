package com.bid.idearush.domain.chat.service;

import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.exception.UserFindException;
import com.bid.idearush.global.exception.errortype.UserFindErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;

    public String getUserNickName(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> {
                    throw new UserFindException(UserFindErrorCode.USER_EMPTY);
                }
        ).getNickname();
    }

}
