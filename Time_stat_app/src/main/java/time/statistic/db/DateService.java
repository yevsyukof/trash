package time.statistic.db;

import java.util.Date;

public class DateService {

    public static String formatDate(Date date) {
        return (date.getYear() + 1900) + "-"
                + (date.getMonth() + 1) + "-" + date.getDate();
    }
}
