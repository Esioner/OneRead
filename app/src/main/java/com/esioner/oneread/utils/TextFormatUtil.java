package com.esioner.oneread.utils;

import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Esioner on 2018/6/12.
 */

public class TextFormatUtil {
    private static final String TAG = TextView.class.getSimpleName();

    public static final String Y_M_D = "yyyy-MM-dd";
    public static final String Y_M = "yyyy-MM";

    /**
     * 格式化时间 yyyy-MM-dd HH:mm:ss ==》 [2018,MAY,10]
     *
     * @param dateString
     * @return
     */
    public static String[] getDateBarDateArray(String dateString) {
        String[] dateArr = new String[3];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            dateArr[0] = year + "";
            String formatMonth = "";
            switch (month) {
                case 0:
                    formatMonth = "JANUARY";
                    break;
                case 1:
                    formatMonth = "FEBRUARY";
                    break;
                case 2:
                    formatMonth = "MARCH";
                    break;
                case 3:
                    formatMonth = "APRIL";
                    break;
                case 4:
                    formatMonth = "MAY";
                    break;
                case 5:
                    formatMonth = "JUNE";
                    break;
                case 6:
                    formatMonth = "JULY";
                    break;
                case 7:
                    formatMonth = "AUGUST";
                    break;
                case 8:
                    formatMonth = "SEPTEMBER";
                    break;
                case 9:
                    formatMonth = "OCTOBER";
                    break;
                case 10:
                    formatMonth = "NOVEMBER";
                    break;
                case 11:
                    formatMonth = "DECEMBER";
                    break;
            }
            dateArr[1] = formatMonth;
            dateArr[2] = day + "";
//            Log.d(TAG, "initData: " + year + "-" + month + "-" + day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "formatDate: dateArr = " + Arrays.toString(dateArr));
        return dateArr;
    }


    /**
     * 获取上个月日期
     *
     * @param dateTime
     * @return
     * @throws ParseException
     */
    public static String getLastMonth(String dateTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(Y_M_D);
        Calendar c = Calendar.getInstance();
        Date date = format.parse(dateTime);
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat(Y_M);
        String lastMonth = sdf.format(c.getTime()); //上月
//        System.out.println(gtimelast);

        return lastMonth;
    }
}
