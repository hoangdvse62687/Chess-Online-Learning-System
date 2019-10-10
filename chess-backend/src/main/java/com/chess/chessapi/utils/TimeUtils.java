package com.chess.chessapi.utils;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class TimeUtils implements Serializable {

    public static Timestamp getCurrentTime(){
        return new Timestamp(new Date().getTime());
    }

    public static long getDurationSecond(Timestamp oldDate, Timestamp newDate){
        long diff = newDate.getTime() - oldDate.getTime();
        return diff / 1000 % 60;
    }

    public static String toCron(Timestamp timestamp){
        LocalDateTime dateTime = timestamp.toLocalDateTime();
        return generateCronExpression(String.valueOf(dateTime.getSecond()),
                String.valueOf(dateTime.getMinute()),
                String.valueOf(dateTime.getHour()),
                String.valueOf(dateTime.getDayOfMonth()),
                String.valueOf(dateTime.getMonth().getValue()),
                String.valueOf(dateTime.getYear()));
    }

    private static String generateCronExpression(final String seconds, final String minutes, final String hours,
                                                 final String dayOfMonth,
                                                 final String month, final String year)
    {
        //allow day of the month not using day of the week
        return String.format("%1$s %2$s %3$s %4$s %5$s %6$s %7$s", seconds, minutes, hours, dayOfMonth
                , month, "?", year);
    }
}
