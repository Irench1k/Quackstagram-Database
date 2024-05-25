package quackstagram.utilities;

import java.sql.*;
import java.util.ArrayList;

import quackstagram.models.User;

public class DatabaseConnector {

    public static Connection connect() {
        Connection myCon = null;
        try {
            String URL = "jdbc:mysql://localhost:3306/quackstagram";
            Class.forName("com.mysql.cj.jdbc.Driver");
            myCon = DriverManager.getConnection(URL, "BCS1510", "BCS1510");
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