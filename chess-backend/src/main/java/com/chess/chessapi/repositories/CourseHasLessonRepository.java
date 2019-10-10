package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.CourseHasLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface CourseHasLessonRepository extends JpaRepository<CourseHasLesson,Long> {
    @Query(value = "Insert into course_has_lesson (lesson_id,course_id,lesson_ordered) " +
            "VALUES(?1,?2,?3)",nativeQuery = true)
    @Modifying
    @Transactional
    void create(long lessonId,long courseId,int lessonOrdered);

    @Query(value = "Select * From course_has_lesson where lesson_id = ?1 and course_id = ?2",nativeQuery = true)
    CourseHasLesson findByLessonIdAndCourseId(long lessonId,long courseId);

    @Query(value = "Delete from course_has_lesson where lesson_id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void deleteAllByLessonId(long lessonId);

    @Query(value = "Select lesson_ordered from course_has_lesson where course_id = ?1 Order by lesson_ordered Desc LIMIT 1",nativeQuery = true)
    Integer findLastestLessonOrderedByCourseId(long courseId);

    @Query(value = "Select course_id From course_has_lesson where lesson_id = ?1",nativeQuery = true)
    List<Long> findListCourseIdByLessonId(long lessonId);

    @Query(value = "Select lesson_id From course_has_lesson where course_id = ?1",nativeQuery = true)
    List<Long> findByCourseId(long courseId);
}
