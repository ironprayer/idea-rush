package com.bid.idearush.global.util;

import com.bid.idearush.domain.idea.repository.IdeaRepository;
import com.bid.idearush.domain.idea.type.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate redisTemplate;

    public Long getIdeaCount() {
        Object redisIdea = redisTemplate.opsForValue().get("ideaCount");
        if(Objects.isNull(redisIdea)){
            return null;
        }
        return Long.parseLong(redisIdea.toString());
    }
    public void setIdeaCount(long count) {
        redisTemplate.opsForValue().set("ideaCount", count);
    }
    public void addIdeaCount() {
        redisTemplate.opsForValue().set("ideaCount", getIdeaCount()+1);
    }
    public void minIdeaCount() {
        redisTemplate.opsForValue().set("ideaCount", getIdeaCount()-1);
    }

    public Long getCategoryCount(Category category) {
        Object redisCategory = redisTemplate.opsForValue().get(category.toString());
        if(Objects.isNull(redisCategory)){
            return null;
        }
        return Long.parseLong(redisCategory.toString());
    }
    public void setCategoryCount(Category category, Long count) {
        redisTemplate.opsForValue().set(category.toString(), count);
    }
    public void addCategoryCount(Category category) {
        redisTemplate.opsForValue().set(category.toString(), getCategoryCount(category)+1);
    }
    public void minCategoryCount(Category category) {
        redisTemplate.opsForValue().set(category.toString(), getCategoryCount(category)-1);
    }

}
