package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "point_log")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="pointLogId",scope = User.class)
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getPointLogPaginationByUserid",
                procedureName = "get_point_log_pagination_by_userid",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "userId",type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "pageIndex",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "pageSize",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "sortBy",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "sortDirection",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.INOUT,name = "totalElements",type = Long.class)
                }
        ),
})
public class PointLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long pointLogId;

    @Length(max = 200, message = "Content shouldn't larger than 200 characters")
    @NotNull(message = "Content is required not null")
    private String content;

    private int point;

    @Column(name = "created_date")
    @NotNull(message = "Created date is required not null")
    private java.sql.Timestamp createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    public long getPointLogId() {
        return pointLogId;
    }

    public void setPointLogId(long pointLogId) {
        this.pointLogId = pointLogId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
