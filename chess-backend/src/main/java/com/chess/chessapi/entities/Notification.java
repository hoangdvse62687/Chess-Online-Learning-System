package com.chess.chessapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "notification")
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(
                name = "getNotificationPagination",
                procedureName = "get_notification_pagination",
                parameters = {
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "role",type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "userId",type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "pageIndex",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "pageSize",type = Integer.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "sortBy",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.IN,name = "sortDirection",type = String.class),
                        @StoredProcedureParameter(mode = ParameterMode.INOUT,name = "totalElements",type = Long.class),
                        @StoredProcedureParameter(mode = ParameterMode.INOUT,name = "totalNotViewedElements",type = Long.class)
                }
        ),
})
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long notificationId;

    @NotNull(message = "Object id must not be null")
    @Column(name = "object_id")
    private long objectId;

    @NotNull(message = "Object name must not be null")
    @Column(name = "object_name")
    @Length(max = 255,message = "ObjectName is required not larger than 255 characters")
    private String objectName;

    @NotNull(message = "Object avatar must not be null")
    @Column(name = "object_avatar")
    @Length(max = 255,message = "Object Avatar is required not larger than 255 characters")
    private String objectAvatar;

    @NotNull(message = "Object Type must not be null")
    @Column(name = "object_type_id")
    private long objectTypeId;

    @Length(max = 500,message = "Content is required not larger than 255 characters")
    private String content;

    @Column(name = "is_viewed")
    private boolean isViewed;

    @Column(name = "created_date")
    private java.sql.Timestamp createDate;

    @NotNull(message = "Role target must not be null")
    @Column(name = "role_target")
    private long roleTarget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public long getObjectTypeId() {
        return objectTypeId;
    }

    public void setObjectTypeId(long objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isViewed() {
        return isViewed;
    }

    public void setViewed(boolean viewed) {
        isViewed = viewed;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public long getRoleTarget() {
        return roleTarget;
    }

    public void setRoleTarget(long roleTarget) {
        this.roleTarget = roleTarget;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getObjectAvatar() {
        return objectAvatar;
    }

    public void setObjectAvatar(String objectAvatar) {
        this.objectAvatar = objectAvatar;
    }
}
