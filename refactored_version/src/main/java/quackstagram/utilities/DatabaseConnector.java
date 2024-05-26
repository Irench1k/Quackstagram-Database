package quackstagram.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

    public static Connection connect() {
        Connection myCon = null;
        try {
            String URL = "jdbc:mysql://localhost:3306/quackstagram";
            Class.forName("com.mysql.cj.jdbc.Driver");
            //myCon = DriverManager.getConnection(URL, "BCS1510", "BCS1510");
            myCon = DriverManager.getConnection(URL, "Vjosa", "Vjosa");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }

        return myCon;
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null)
                resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (statement != null)
                statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}