package com.swifty.fillcolor.util;

import java.util.Calendar;

/**
 * Created by Swifty.Wang on 2015/9/4.
 */
public class DateTimeUtil {
    public static String formatTimeStamp(long l) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(l);
        return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
    }
}
