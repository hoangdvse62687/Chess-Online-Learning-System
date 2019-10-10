package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface CertificatesRepository extends JpaRepository<Certificate,Long> {
    @Query(value = "Select * From certificates c where c.user_id = ?1",nativeQuery = true)
    List<Certificate> findAllByUserId(long userId);

    @Query(value = "Insert into certificates (certificate_link,user_id) values (?1,?2)",nativeQuery = true)
    @Modifying
    @Transactional
    void create(String link,long userId);
}
