package com.chess.chessapi.repositories;

import com.chess.chessapi.entities.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointLogRepository extends JpaRepository<PointLog,Long> {

}
