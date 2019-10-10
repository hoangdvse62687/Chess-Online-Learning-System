package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.CategoryHasCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CategoryHasCourseRepository extends JpaRepository<CategoryHasCourse,Long> {
    @Modifying
    @Transactional
    @Query(value = "Insert into category_has_course (category_id,course_id) " +
            "VALUES (?1,?2)"
            ,nativeQuery = true)
    void create(long categoryId,long courseId);

    @Query(value = "Select * From category_has_course where course_id = ?1"
            ,nativeQuery = true)
    List<CategoryHasCourse> findAllByCourseId(long courseId);
}
