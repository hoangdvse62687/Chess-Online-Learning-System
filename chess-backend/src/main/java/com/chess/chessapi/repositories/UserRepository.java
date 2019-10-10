package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active From users u where u.email = ?1",nativeQuery = true)
    Optional<Object> findByEmailCustom(String email);

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active From users u where u.id = ?1",nativeQuery = true)
    Optional<Object> findByIdCustom(long id);

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active,u.avatar,u.full_name,u.created_date " +
            "From users u",
            countQuery = "Select count(u.id) from users u",
            nativeQuery = true)
    Page<Object> findAllCustom(Pageable pageable);

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active,u.avatar,u.full_name,u.created_date " +
            "From users u WHERE u.email like ?1",
            countQuery = "Select count(u.id) from users u WHERE u.email like ?1",
            nativeQuery = true)
    Page<Object> findAllByFullNameCustom(Pageable pageable,String email);

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active,u.avatar,u.full_name,u.created_date " +
            "From users u WHERE u.email like ?1 and u.role_id like ?2",
            countQuery = "Select count(u.id) from users u WHERE u.email like ?1 and u.role_id like ?2",
            nativeQuery = true)
    Page<Object> findAllByFullNameFilterRole(Pageable pageable,String email,String role);

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active,u.avatar,u.full_name,u.created_date " +
            "From users u WHERE u.email like ?1 and u.is_active = ?2",
            countQuery = "Select count(u.id) from users u WHERE u.email like ?1 and u.is_active = ?2",
            nativeQuery = true)
    Page<Object> findAllByFullNameFilterStatus(Pageable pageable,String email,boolean isActive);

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active,u.avatar,u.full_name,u.created_date " +
            "From users u WHERE u.email like ?1 and u.role_id like ?2 and u.is_active = ?3",
            countQuery = "Select count(u.id) from users u WHERE u.email like ?1 and u.role_id like ?2 and u.is_active = ?3",
            nativeQuery = true)
    Page<Object> findAllByFullNameFilterRoleAndStatus(Pageable pageable,String email,String role,boolean isActive);

    @Query(value = "Select u.id,u.email,u.role_id,u.is_active,u.avatar,u.full_name,u.created_date " +
            "From users u WHERE u.email like ?1 and u.role_id like ?2 and u.is_active = ?3 and u.is_reviewed = ?4",
            countQuery = "Select count(u.id) from users u WHERE u.email like ?1 and u.role_id like ?2 and u.is_active = ?3 and u.is_reviewed = ?4",
            nativeQuery = true)
    Page<Object> findAllByFullNameFilterRoleAndStatusAndReviewed(Pageable pageable,String email,String role,boolean isActive,boolean isReviewed);

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "Update users u Set u.is_active = ?2,u.is_reviewed = ?3 Where u.id = ?1"
            ,nativeQuery = true)
    void updateStatus(long id,boolean isActive,boolean isReviewed);

    @Modifying
    @Transactional
    @Query(value = "Update users u Set u.full_name = ?2,u.achievement = ?3,u.avatar = ?4,u.modified_date = ?5 Where u.id = ?1"
            ,nativeQuery = true)
    void updateProfile(long id,String name,String achievement,String avatar, Timestamp modifiedDate);

    @Modifying
    @Transactional
    @Query(value = "Update users u Set u.full_name = ?2,u.achievement = ?3,u.point = ?4" +
            ",u.role_id = ?5,u.is_active = ?6,u.avatar = ?7,u.is_reviewed = ?8,u.modified_date = ?9 Where u.id = ?1"
            ,nativeQuery = true)
    void updateRegister(long id, String name, String achievement, int point, long role, boolean isActive, String avatar
            , boolean isReviewed, Timestamp modifiedDate);

    @Modifying
    @Transactional
    @Query(value = "Update users u Set u.point = u.point + ?2 Where u.id = ?1"
            ,nativeQuery = true)
    void increasePoint(long id,int point);

    @Query(value = "Select point From users where id = ?1",nativeQuery = true)
    Integer findPointByUserId(long id);

    @Modifying
    @Transactional
    @Query(value = "Delete from users where id = ?1"
            ,nativeQuery = true)
    void deleteUser(long userId);

    @Query(value = "Select id From users where role_id = ?1",nativeQuery = true)
    List<Long> findAllListUserIdsByRole(long roleId);
}
