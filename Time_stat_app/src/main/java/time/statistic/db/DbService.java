package time.statistic.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbService {
    public static final String DB_URL = "jdbc:h2:C:/Users/yevsyukof/Desktop/Time_stat_app/ts_db/h2db";
//    public static final String DB_URL = "jdbc:h2:<здесь нужно прописать путь до ts_db/h2db>";

    public static final String DB_Driver = "org.h2.Driver";

    public DbService() {
        try {
            Class.forName(DB_Driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void insertEntry(String activityName, Long longitude, String date) {
        try {
            var connection = DriverManager.getConnection(DB_URL);
            var statement = connection.createStatement();
            var sqlRes = statement.execute(
                    "INSERT INTO STATISTICA (ACTIVITY_NAME, LONGITUDE, DATE) VALUES ('"
                            + activityName + "', " + longitude.toString() + ", '" + date + "')");

            statement.close();
            connection.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static List<Long> getData(String activityName, String from, String to) {
        ArrayList<Long> result = new ArrayList<>();
        try {
            var connection = DriverManager.getConnection(DB_URL);
            var statement = connection.createStatement();
            var sqlRes = statement.executeQuery(
                    "SELECT * FROM STATISTICA " +
                            "WHERE (date >= '" + from + "')" +
                            " AND (date <= '" + to + "') " +
                            " AND ( ACTIVITY_NAME = '" + activityName + "')");

            while (sqlRes.next()) {
                result.add(sqlRes.getLong("LONGITUDE"));
            }

            statement.close();
            connection.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    public static List<String> getActivitiesSet() {
        ArrayList<String> result = new ArrayList<>();
        try {
            var connection = DriverManager.getConnection(DB_URL);
            var statement = connection.createStatement();
            var sqlRes = statement.executeQuery(
                    "SELECT DISTINCT ACTIVITY_NAME FROM STATISTICA ORDER BY ACTIVITY_NAME");

            while (sqlRes.next()) {
                result.add(sqlRes.getString("ACTIVITY_NAME"));
//                sqlRes.
            }

            statement.close();
            connection.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

}
