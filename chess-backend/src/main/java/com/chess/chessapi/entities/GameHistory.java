package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "game_history")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="gamehistoryId",scope = GameHistory.class)
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getRateWinnableReport",
                procedureName = "get_rate_winnable_report",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "year",type = Integer.class)
                }
        ),
        @NamedStoredProcedureQuery(
                name = "getRateWinnableLevelReport",
                procedureName = "get_rate_winnable_level_report",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "year",type = Integer.class),
                }
        )
})
public class GameHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long gamehistoryId;

    @NotNull(message = "Start time must not be null")
    @Column(name = "start_time")
    private Timestamp startTime;

    @NotNull(message = "Record must not be null")
    private String record;

    @NotNull(message = "Level must not be null")
    private int level;

    @NotNull(message = "Color must not be null")
    private int color;

    @NotNull(message = "Game time must not be null")
    @Column(name = "game_time")
    private int gameTime;

    @NotNull(message = "Point must not be null")
    private float point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private int status;

    public long getGamehistoryId() {
        return gamehistoryId;
    }

    public void setGamehistoryId(long gamehistoryId) {
        this.gamehistoryId = gamehistoryId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public float getPoint() {
        return point;
    }

    public void setPoint(float point) {
        this.point = point;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
