package com.aacs.todoaacs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtility {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static SimpleDateFormat format;

    public static Date stringToDate(String pattern, String date) throws ParseException {
        format = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return format.parse(date);
    }

    public static String dateToString(String patten, Date date){
        format = new SimpleDateFormat(patten, Locale.ENGLISH);
        return format.format(date);
    }
}
