package services.timeservice;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Clock {
    private static final SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm:ss");

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

    public static String date() {
        return dateFormatter.format(new Date());
    }

    public static String time() {
        return timeFormatter.format(new Date());
    }
}
