package com.cworld.notie.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FormatHelper {
    public static String text(String text) {
        return text.replace("\u3000", " ").trim();
    }

    public static String time(Date lastEditTime) {
        Date currentTime = Calendar.getInstance().getTime();
        long duration = (currentTime.getTime() - lastEditTime.getTime()) / 1000;

        if (duration < 0) {
            return "Future";
        } else if (duration < 60) {
            return duration + " seconds ago";
        } else if (duration < 3600) {
            long minutes = duration / 60;
            return minutes + " minutes ago";
        } else if (duration < 86400) {
            long hours = duration / 3600;
            return hours + " hours ago";
        } else if (duration < 2592000) {
            long days = duration / 86400;
            if (days == 1) {
                return "Yesterday";
            } else {
                return days + " days ago";
            }
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            return formatter.format(lastEditTime);
        }
    }
}
