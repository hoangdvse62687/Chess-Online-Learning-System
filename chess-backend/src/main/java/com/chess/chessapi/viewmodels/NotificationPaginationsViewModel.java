package com.chess.chessapi.viewmodels;

import com.chess.chessapi.entities.Notification;
import com.chess.chessapi.models.PagedList;

public class NotificationPaginationsViewModel {
    PagedList<Notification> data;
    private long totalNotViewedElements;

    public NotificationPaginationsViewModel(PagedList<Notification> data, long totalNotViewedElements) {
        this.data = data;
        this.totalNotViewedElements = totalNotViewedElements;
    }

    public PagedList<Notification> getData() {
        return data;
    }

    public void setData(PagedList<Notification> data) {
        this.data = data;
    }

    public long getTotalNotViewedElements() {
        return totalNotViewedElements;
    }

    public void setTotalNotViewedElements(long totalNotViewedElements) {
        this.totalNotViewedElements = totalNotViewedElements;
    }
}
