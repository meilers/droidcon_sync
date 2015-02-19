package com.frankandoak.synchronization.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mj_eilers on 15-02-18.
 */
public class DateUtil {

    public static Calendar convertToDate(String date)
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
            Date convertedDate = new Date();
            try {
                convertedDate = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(convertedDate);

            return calendar;
        }
        catch (Exception e) {
//            e.printStackTrace();
            return null;
        }

    }

    public static String convertToString(Calendar date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") {
            public StringBuffer format(Date date, StringBuffer toAppendTo, java.text.FieldPosition pos) {
                StringBuffer toFix = super.format(date, toAppendTo, pos);
                return toFix.insert(toFix.length()-2, ':');
            };
        };

        return sdf.format(date.getTime());

    }
}
