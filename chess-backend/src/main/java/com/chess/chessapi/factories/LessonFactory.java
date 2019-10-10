package com.chess.chessapi.factories;

import com.chess.chessapi.interfaces.LessonInterface;
import com.chess.chessapi.constants.ObjectType;
import com.chess.chessapi.models.Exercise;
import com.chess.chessapi.models.InteractiveLesson;
import com.chess.chessapi.models.UninteractiveLesson;

public class LessonFactory{

    public Object getContent(String content,int lessonType){
        LessonInterface lesson = this.getInstance(lessonType);
        return lesson.getContent(content);
    }

    public String toJson(Object entity,int lessonType){
        LessonInterface lesson = this.getInstance(lessonType);
        return lesson.toJson(entity);
    }

    private LessonInterface getInstance(int lessonType){
        LessonInterface lesson = new InteractiveLesson();
        switch (lessonType){
            case ObjectType.INTERACTIVE_LESSON:
                lesson = new InteractiveLesson();
                break;
            case ObjectType.UNINTERACTIVE_LESSON:
                lesson = new UninteractiveLesson();
                break;
            case ObjectType.EXERCISE:
                lesson = new Exercise();
                break;
        }
        return lesson;
    }
}
