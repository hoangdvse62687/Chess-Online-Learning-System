package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;


@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query(value = "SELECT If(user_id = ?2,'true','false') From review Where id = ?1",nativeQuery = true)
    Boolean checkPermissionModifyReview(long reviewId,long userId);

    @Modifying
    @Transactional
    @Query(value = "Update review Set content = ?2,rating = ?3,modified_date = ?4 Where id = ?1",nativeQuery = true)
    void update(long reviewId, String content, float rating, Timestamp modifiedDate);

    @Modifying
    @Transactional
    @Query(value = "Delete From review Where id = ?1",nativeQuery = true)
    void remove(long reviewId);

    @Query(value = "Select If(count(id) > 0,'true','false') From review where user_id = ?1 and course_id = ?2",nativeQuery = true)
    Boolean checkIsComment(long userId,long courseId);

    @Query(value = "Select If(count(id) > 0,rating,0) From review where user_id = ?1 and course_id = ?2",nativeQuery = true)
    Integer findRatingByUserIdAndCourseId(long userId,long course);
}
