package time.statistic;


import time.statistic.db.DateService;
import time.statistic.db.DbService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
//        DbService.getActivitiesSet();

        TimeStatApp.getInstance().run();
    }

}

