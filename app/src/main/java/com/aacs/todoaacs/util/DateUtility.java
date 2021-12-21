package com.aacs.todoaacs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtility {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static SimpleDateFormat format;

    public static Date stringToDate(String pattern, String date) throws ParseException {
        format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return format.parse(date);
    }

    public static String dateToString(String pattern, Date date) {
        format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return format.format(date);
    }

    public static Date today(String pattern) {
        format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        String formattedDate = format.format(Calendar.getInstance().getTime());
        Date today = null;
        try {
            today = format.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return today;
    }
}
