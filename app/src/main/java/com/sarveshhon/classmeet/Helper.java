package com.sarveshhon.classmeet;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Helper {

    public static String DEFAULT_BATCH = "4";

    public static int sTime[] = {915, 955, 1045, 1125, 1230, 1310};
    public static int eTime[] = {955, 1035, 1125, 1205, 1310, 1350};


    public static String getSlot() {
        String slot = "0";

        Calendar calendar1 = Calendar.getInstance();
        SimpleDateFormat formatter1 = new SimpleDateFormat("HHmm");

        int cTime = Integer.parseInt(formatter1.format(calendar1.getTime()));

        if ((cTime >= sTime[0] && cTime < eTime[0])) {
            slot = "1";
        } else if ((cTime >= sTime[1] && cTime < eTime[1])) {
            slot = "2";
        } else if ((cTime >= sTime[2] && cTime < eTime[2])) {
            slot = "3";
        } else if ((cTime >= sTime[3] && cTime < eTime[3])) {
            slot = "4";
        } else if ((cTime >= sTime[4] && cTime < eTime[4])) {
            slot = "5";
        } else if ((cTime >= sTime[5] && cTime < eTime[5])) {
            slot = "6";
        }


        return slot;
    }

    public static String getDay() {
        String today = "Monday";
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                today = "Monday";
                break;
            case Calendar.TUESDAY:
                today = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                today = "Wednesday";
                break;
            case Calendar.THURSDAY:
                today = "Thursday";
                break;
            case Calendar.FRIDAY:
                today = "Friday";
                break;
        }

        return today;
    }

}
