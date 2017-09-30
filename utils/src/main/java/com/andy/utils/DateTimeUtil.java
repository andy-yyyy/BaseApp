/*
 * Copyright (C) 2017 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.andy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lixn on 2017/9/4.
 */

public class DateTimeUtil {

    public static final String PATTERN_SERVER = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String PATTERN_LOCAL_DATE = "yyyy-MM-DD";
    public static final String PATTERN_LOCAL_TIME = "HH:mm:ss";

    public static final int DELTA_MIN = 60;
    public static final int DELTA_HOUR = DELTA_MIN*60;
    public static final int DELTA_DAY = DELTA_HOUR*24;

    public static String formatDate(String dateStr) {
        Date date = parseDateFromServer(dateStr);
        if (date == null) {
            return "";
        }
        Date now = new Date();
        long delta = (now.getTime() - date.getTime()) / 1000;  // 距离目前的时间（秒）
        if (delta < DELTA_MIN) {
            return "seconds ago";
        } else if (delta <DELTA_HOUR) {
            int m = (int) (delta / DELTA_MIN);
            return m + "minutes ago";
        } else if (delta < DELTA_DAY) {
            int h = (int) (delta / DELTA_HOUR);
            return h + "hours ago";
        } else {
            SimpleDateFormat df = new SimpleDateFormat(PATTERN_LOCAL_DATE, Locale.getDefault());
            return df.format(date);
        }
    }

    public static String formatTime(String dateStr) {
        Date date = parseDateFromServer(dateStr);
        if (date == null) {
            return "";
        }
        SimpleDateFormat df = new SimpleDateFormat(PATTERN_LOCAL_TIME, Locale.getDefault());
        return df.format(date);
    }

    /**
     * 将服务器的时间格式解析成Date
     * @param dateStr
     * @return
     */
    public static Date parseDateFromServer(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat(PATTERN_SERVER, Locale.CHINA);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
