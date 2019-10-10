package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;


@Repository
public interface LessonRepository extends JpaRepository<Lesson,Long> {
    @Query(value = "Select If(count(l.owner) > 0,l.owner,0) From lesson l where l.id = ?1",nativeQuery = true)
    Long findLessonAuthorByLessonId(long lessonId);

    @Query(value = "Update lesson l Set l.name = ?2,l.content = ?3,l.description = ?4,l.modified_date = ?5 where id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void update(long lessonId, String name,String content, String description, Timestamp modifiedDate);
}
