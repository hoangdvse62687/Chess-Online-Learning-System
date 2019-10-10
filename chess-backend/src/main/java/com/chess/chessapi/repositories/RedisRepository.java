package com.chess.chessapi.repositories;

import java.util.Map;

public interface RedisRepository<T>{
    void save(T entity);

    T find(Long id);

    Map<Long,T> findAll();

    void update(T entity);

    void deleteById(Long id);
}
