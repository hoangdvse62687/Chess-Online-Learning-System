package com.chess.chessapi.services;

import com.chess.chessapi.models.ChessGame;
import com.chess.chessapi.repositories.RedisRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RedisChessGameService implements RedisRepository<ChessGame> {
    private RedisTemplate<String, Object> redisTemplate;
    private ListOperations<String, Object> listOperations;
    private Gson gson = new Gson();

    @Autowired
    public RedisChessGameService(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
    }

    @Override
    public void save(ChessGame entity) {
        this.listOperations.leftPush(entity.getGameHistoryId().toString(),gson.toJson(entity));
    }

    @Override
    public ChessGame find(Long id) {
        Object data = this.listOperations.index(id.toString(),-1);
        if(data == null){
            return null;
        }else{
            return gson.fromJson(data.toString(),ChessGame.class);
        }
    }

    @Override
    public Map<Long, ChessGame> findAll() {
        Set<String> keys = this.redisTemplate.keys("*");
        Map<Long,ChessGame> data = new HashMap<>();
        for (String key:
                keys){
            data.put(Long.parseLong(key),gson.fromJson(this.listOperations.index(key,-1)
                    .toString(),ChessGame.class));
        }
        return data;
    }

    @Override
    public void update(ChessGame entity) {
        this.deleteById(entity.getGameHistoryId());
        this.listOperations.leftPush(entity.getGameHistoryId().toString(),gson.toJson(entity));
    }

    @Override
    public void deleteById(Long id) {
        this.redisTemplate.delete(id.toString());
    }
}
