package org.z1god.absenguruprivate.Utilitis;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyDate {
    public static String[] getCurrentDateAndTime() {
        String[] currentDateAndTime = new String[2];

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date current = new Date();

        currentDateAndTime[0] = dateFormat.format(current);
        currentDateAndTime[1] = timeFormat.format(current);

        return currentDateAndTime;
    }

    public static String convertDateToName(String date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy", Locale.getDefault());
        try {
            return sdf.format(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertTime(String time){
        if (time.equalsIgnoreCase("00:00:00")) return "Belum Logout";
        return time;
    }
}
