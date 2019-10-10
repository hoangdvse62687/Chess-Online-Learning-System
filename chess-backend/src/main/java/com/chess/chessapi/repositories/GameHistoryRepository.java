package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.GameHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameHistoryRepository extends JpaRepository<GameHistory,Long> {
    @Query(value = "Select id,start_time,level,game_time,point,status from game_history where user_id = ?1",
            countQuery = "Select count(id) from game_history where user_id = ?1",
            nativeQuery = true)
    Page<Object> findAllByUserId(Pageable pageable, long userId);

    @Query(value = "Select count(id) from game_history where user_id = ?1 and status =?2",nativeQuery = true)
    Integer countByUserIdAndStatus(long userId,int status);

    @Query(value = "Select count(id) from game_history where user_id = ?1",nativeQuery = true)
    Integer countByUserId(long userId);

    @Query(value = "Select status from game_history where user_id = ?1 Order by id Desc LIMIT 1",nativeQuery = true)
    Integer findLastGameHistoryStatusByUserId(long userId);

    @Query(value = "Select * from game_history where user_id = ?1 Order by id Desc LIMIT 1",nativeQuery = true)
    GameHistory findLastGameHistoryByUserId(long userId);
}
