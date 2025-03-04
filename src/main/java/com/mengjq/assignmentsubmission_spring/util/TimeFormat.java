package com.mengjq.assignmentsubmission_spring.util;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;


@Data
public class TimeFormat {
    public static String getNowTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//设置北京时间
        return simpleDateFormat.format(new Date());
    }
    public static String getNowTimeForFileName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//设置北京时间
        return simpleDateFormat.format(new Date());
    }

    public static String verifyNull(String time) {
        // if registerTime === "null" then return null
//        System.out.println("time: " + time);
        // if time is null or is False or other value similar with false then return null
        if (time == null || time.equals("null") || Objects.equals(time, "")) return null;
        else {
            return time;
        }
    }

    public static String verifyTimeOrDefault(String time) {
        if (verifyNull(time) == null){
            return "2099-01-01 00:00:00";}
        else {
            return time;
        }
    }

    public static String getBeforeNowDays(int days) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//设置北京时间
        Date date = new Date();
        long time = date.getTime() - (long) days * 24 * 60 * 60 * 1000;
        date.setTime(time);
        return simpleDateFormat.format(date);
    }
}
