package training.ruseff.com.karateqrscanner.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import training.ruseff.com.karateqrscanner.models.User;

public class Utils {

    public static boolean isNullOrEmpty(String string) {
        return string == null || "".equals(string);
    }

    public static boolean isUserValid(User user) {
        return user != null && !isNullOrEmpty(user.getName()) && user.getInternalId() != -1;
    }

    public static int getYearFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonthFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static String getDateFormatted(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", new Locale("bg", ""));
        return format.format(date);
    }

    public static String datesToString(Date[] dates) {
        StringBuilder sb = new StringBuilder();
        for (Date d : dates) {
            sb.append("\t");
            sb.append(getDateFormatted(d));
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String getTimestampFromDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

}
