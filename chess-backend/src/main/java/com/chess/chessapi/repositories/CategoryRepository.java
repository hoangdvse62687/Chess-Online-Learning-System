package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Transactional
    @Modifying
    @Query(value = "Update category Set name = ?2 where id = ?1",nativeQuery = true)
    void update(long categoryId,String name);
}
