package com.chess.chessapi.services;

import com.chess.chessapi.models.CategoryRedis;
import com.chess.chessapi.repositories.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class RedisCategoryService implements RedisRepository<CategoryRedis> {
    private static final String KEY = "CATEGORIES";

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Long, CategoryRedis> hashOperations;

    @Autowired
    public RedisCategoryService(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(CategoryRedis categoryRedis){
        hashOperations.put(KEY,categoryRedis.getCategoryId(),categoryRedis);
    }

    @Override
    public CategoryRedis find(Long id) {
        return hashOperations.get(KEY,id);
    }

    @Override
    public Map<Long, CategoryRedis> findAll() {
        return hashOperations.entries(KEY);
    }

    @Override
    public void update(CategoryRedis entity) {
        hashOperations.put(KEY,entity.getCategoryId(),entity);
    }

    @Override
    public void deleteById(Long id) {
        hashOperations.delete(KEY,id);
    }
}
