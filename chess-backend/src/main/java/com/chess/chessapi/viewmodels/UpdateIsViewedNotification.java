package com.chess.chessapi.viewmodels;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UpdateIsViewedNotification {
    @NotNull(message = "Notification Ids must not be null")
    private List<Long> notificationIds;

    public List<Long> getNotificationIds() {
        return notificationIds;
    }

    public void setNotificationIds(List<Long> notificationIds) {
        this.notificationIds = notificationIds;
    }
}
