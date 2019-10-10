package com.chess.chessapi.interfaces;

public interface LessonInterface<T> {
    T getContent(String content);

    String toJson(T entity);
}
