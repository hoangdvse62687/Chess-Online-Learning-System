package com.chess.chessapi.services;
import com.chess.chessapi.models.ClientIdRedis;
import com.chess.chessapi.repositories.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RedisClientIdService implements RedisRepository<ClientIdRedis> {
    private static final String KEY = "CLIENT_IDS";

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Long, ClientIdRedis> hashOperations;

    @Autowired
    public RedisClientIdService(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(ClientIdRedis clientIdRedis){
        hashOperations.put(KEY,clientIdRedis.getUserId(),clientIdRedis);
    }

    @Override
    public ClientIdRedis find(Long id) {
        return hashOperations.get(KEY,id);
    }

    @Override
    public Map<Long, ClientIdRedis> findAll() {
        return hashOperations.entries(KEY);
    }

    @Override
    public void update(ClientIdRedis entity) {
        hashOperations.put(KEY,entity.getUserId(),entity);
    }

    @Override
    public void deleteById(Long id) {
        hashOperations.delete(KEY,id);
    }

    public List<String> findAllClientIdByRole(long role){
        List<String> clientIds = new ArrayList<>();
        this.findAll().forEach((key,value) -> {
            if(value.getRoleId() == role){
                clientIds.add(value.getClientId());
            }
        });
        return clientIds;
    }
}

