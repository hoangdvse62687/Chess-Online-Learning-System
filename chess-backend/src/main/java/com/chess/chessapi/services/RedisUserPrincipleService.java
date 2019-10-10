package com.chess.chessapi.services;

import com.chess.chessapi.models.UserSecurityRedis;
import com.chess.chessapi.repositories.RedisRepository;
import com.chess.chessapi.security.UserPrincipal;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisUserPrincipleService implements RedisRepository<UserPrincipal> {

    private RedisTemplate<String, Object> redisTemplate;
    private ListOperations<String, Object> listOperations;
    private Gson gson = new Gson();
    private final int SESSION_EXPIRE_MINUTES = 30;

    @Autowired
    public RedisUserPrincipleService(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        listOperations = redisTemplate.opsForList();
    }

    @Override
    public void save(UserPrincipal userPrincipal){
        this.listOperations.leftPush(userPrincipal.getId().toString(),gson.toJson(this.castRedisUserSecurity(userPrincipal)));
        this.redisTemplate.expire(userPrincipal.getId().toString(),SESSION_EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public UserPrincipal find(Long id) {
        Object data = this.listOperations.index(id.toString(),-1);
        if(data == null){
            return null;
        }else{
            this.redisTemplate.expire(id.toString(),SESSION_EXPIRE_MINUTES, TimeUnit.MINUTES);
            return UserPrincipal.create(gson.fromJson(data.toString(),UserSecurityRedis.class));
        }
    }

    @Autowired
    public Map<Long,UserPrincipal> findAll() {
        Set<String> keys = this.redisTemplate.keys("*");
        Map<Long,UserPrincipal> data = new HashMap<>();
        for (String key:
             keys){
            data.put(Long.parseLong(key),UserPrincipal.create(gson.fromJson(this.listOperations.index(key,-1)
                    .toString(),UserSecurityRedis.class)));
        }
        return data;
    }

    @Override
    public void update(UserPrincipal entity) {
        this.deleteById(entity.getId());
        this.listOperations.leftPush(entity.getId().toString(),gson.toJson(this.castRedisUserSecurity(entity)));
        this.redisTemplate.expire(entity.getId().toString(),SESSION_EXPIRE_MINUTES, TimeUnit.MINUTES);
    }

    @Override
    public void deleteById(Long id) {
        this.redisTemplate.delete(id.toString());
    }

    private UserSecurityRedis castRedisUserSecurity(UserPrincipal userPrincipal){
        UserSecurityRedis userSecurityRedis = new UserSecurityRedis();
        userSecurityRedis.setUserId(userPrincipal.getId());
        userSecurityRedis.setEmail(userPrincipal.getEmail());
        userSecurityRedis.setActive(userPrincipal.isStatus());
        userSecurityRedis.setRoleId(Long.parseLong(userPrincipal.getRole()));
        return userSecurityRedis;
    }
}
