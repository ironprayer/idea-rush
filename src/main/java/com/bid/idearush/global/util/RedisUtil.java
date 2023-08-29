package com.bid.idearush.global.util;

import com.bid.idearush.domain.idea.repository.IdeaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate redisTemplate;

    public Long getIdeaCount() {
        Object object = redisTemplate.opsForValue().get("count");
        if(Objects.isNull(object)){
            return null;
        }
        return Long.parseLong(redisTemplate.opsForValue().get("count").toString());
    }

    public void setIdeaCount(long count) {
        redisTemplate.opsForValue().set("count", count);
    }

}
