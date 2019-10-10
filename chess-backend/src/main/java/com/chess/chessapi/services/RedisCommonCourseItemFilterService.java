package com.chess.chessapi.services;

import com.chess.chessapi.models.CommonCourseItemSuggestionRedis;
import com.chess.chessapi.repositories.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class RedisCommonCourseItemFilterService implements RedisRepository<CommonCourseItemSuggestionRedis> {
    private static final String KEY = "COMMON_COURSE_SUGGESTIONS";
    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Long, CommonCourseItemSuggestionRedis> hashOperations;

    @Autowired
    public RedisCommonCourseItemFilterService(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(CommonCourseItemSuggestionRedis entity){
        hashOperations.put(KEY,entity.getCourseId(),entity);
    }

    @Override
    public CommonCourseItemSuggestionRedis find(Long id) {
        return hashOperations.get(KEY,id);
    }

    @Override
    public Map<Long, CommonCourseItemSuggestionRedis> findAll() {
        return hashOperations.entries(KEY);
    }

    @Override
    public void update(CommonCourseItemSuggestionRedis entity) {
        hashOperations.put(KEY,entity.getCourseId(),entity);
    }

    @Override
    public void deleteById(Long id) {
        hashOperations.delete(KEY,id);
    }
}
