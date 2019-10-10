package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;


@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    @Query(value = "Update course c Set c.status_id = ?2,c.modified_date = ?3 where c.id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void updateStatus(long courseId, long statusId, Timestamp modifiedDate);

    @Query(value = "Update course c Set c.name = ?2,c.description = ?3,c.status_id = ?4,c.image = ?5,c.required_elo=?6" +
            ",c.modified_date = ?7" +
            " where c.id = ?1",nativeQuery = true)
    @Modifying
    @Transactional
    void updateCourse(long courseId,String name,String description,
                      long statusId,String image,int requiredElo, Timestamp modifiedDate);

    @Query(value = "Select c.owner From course c where id = ?1",nativeQuery = true)
    Long findAuthorIdByCourseId(long courseId);

    @Query(value = "Select c.id,c.name,c.image From course c where c.id in ?1",nativeQuery = true)
    List<Object[]> findCourseDetailForNotificationByListCourseId(List<Long> listCourseId);

    @Query(value = "Select c.id,c.name,c.image From course c where c.id = ?1",nativeQuery = true)
    Object findCourseDetailForNotificationByCourseId(Long listCourseId);

    @Query(value = "Select id From course where status_id = ?1",nativeQuery = true)
    List<Long> findListCourseIdsByStatus(long statusId);

    @Query(value = "Select c.id,c.name,c.description,c.created_date,c.required_elo,c.status_id,c.image,c.owner,c.modified_date" +
            " From course c Inner join user_has_course userHasCourse On c.id = userHasCourse.course_id " +
            "where userHasCourse.user_id = ?1 Order by userHasCourse.id Desc LIMIT 1",nativeQuery = true)
    Course findLastCourseEnrollByUserId(long userId);

    @Query(value = "Select " +
            "course.id as course_id,course.name,course.status_id,course.image,course.description,course.created_date " +
            ",course.required_elo,users.id as user_id,users.full_name,users.avatar, " +
            "If( ?2 = 0 " +
            ",0,If(Exists (Select id From user_has_course userHasCourse Where userHasCourse.user_id = ?2 and userHasCourse.course_id = course.id),1,0)) as isEnrolled " +
            ",(Select Group_concat(categoryHasCourse.category_id) " +
            "From chessdb.category_has_course categoryHasCourse " +
            "Where categoryHasCourse.course_id = course.id) as categoryIds " +
            ",(Select If(count(r.id) > 0,TRUNCATE( SUM(r.rating)/count(r.id) , 1),0) " +
            "FROM review r " +
            "where r.course_id = course.id ) as rating " +
            ",(Select count(r.id) " +
            "FROM review r " +
            "where r.course_id = course.id ) as totalRating " +
            ",(Select If(count(courseHasLesson.id) > 0 , ((Select count(log.id) From chessdb.learning_log log where log.user_id = ?2 and log.course_id = course.id)/count(courseHasLesson.id)) ,0) " +
            "From course_has_lesson courseHasLesson " +
            "Where courseHasLesson.course_id = course.id) as learningProcessPercent " +
            "From course course " +
            "Inner Join users users " +
            "On users.id = course.owner " +
            "Where not Exists (Select id From user_has_course userHasCourse Where userHasCourse.user_id = ?2 and userHasCourse.course_id = course.id)" +
            "and course.status_id = ?1",nativeQuery = true)
    List<Object[]> findListCourseSuggestionByStatusId(long statusId,long userId);
}
