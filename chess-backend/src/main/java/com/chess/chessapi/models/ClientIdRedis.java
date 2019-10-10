package com.chess.chessapi.models;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;

public class ClientIdRedis extends JdkSerializationRedisSerializer implements Serializable {
    private long userId;
    private long roleId;
    private String clientId;
    private static final long serialVersionUID = 1L;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
