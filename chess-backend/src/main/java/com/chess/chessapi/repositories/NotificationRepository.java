package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Notification;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query(value = "Update notification Set is_viewed = 1 where id in ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void updateIsViewed(List<Long> listIds);
}
