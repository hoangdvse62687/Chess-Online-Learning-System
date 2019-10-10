package com.chess.chessapi.services;

import com.chess.chessapi.models.CourseSuggestionRedis;
import com.chess.chessapi.repositories.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class RedisCourseSuggestionService implements RedisRepository<CourseSuggestionRedis> {
    private static final String KEY = "COURSE_SUGGESTIONS";
    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Long, CourseSuggestionRedis> hashOperations;

    @Autowired
    public RedisCourseSuggestionService(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(CourseSuggestionRedis entity){
        hashOperations.put(KEY,entity.getUserId(),entity);
    }

    @Override
    public CourseSuggestionRedis find(Long id) {
        return hashOperations.get(KEY,id);
    }

    @Override
    public Map<Long, CourseSuggestionRedis> findAll() {
        return hashOperations.entries(KEY);
    }

    @Override
    public void update(CourseSuggestionRedis entity) {
        hashOperations.put(KEY,entity.getUserId(),entity);
    }

    @Override
    public void deleteById(Long id) {
        hashOperations.delete(KEY,id);
    }
}
