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
            myCon = DriverManager.getConnection(URL, "irina", "irina");
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

    // // Everythid for user (45 - 130)
    // public static User getUser(String username) throws Exception {
    //     Connection connection = null;
    //     Statement statement = null;
    //     ResultSet resultSet = null;

    //     try {
    //         connection = DatabaseConnector.connect();
    //         statement = connection.createStatement();
    //         String query = "SELECT * FROM Users WHERE username = '" + username + "'";
    //         resultSet = statement.executeQuery(query);
    //         if (resultSet.next()) {
    //             String password = resultSet.getString("password");
    //             String bio = resultSet.getString("bio");

    //             ArrayList<String> followingUsers = getFollowingUser(username);
    //             int followersCount = getFollowersCount(username);
    //             int postsCount = getPostCount(username);
    //             return new User(username, password, bio, followingUsers, followersCount, postsCount);
    //         } else {
    //             throw new Exception("No such user " + username + " exists");
    //         }
    //     } finally {
    //         DatabaseConnector.close(connection, statement, resultSet);
    //     }
    // }

    // public static ArrayList<String> getFollowingUser(String username) throws Exception {
    //     Connection connection = null;
    //     Statement statement = null;
    //     ResultSet resultSet = null;
    //     ArrayList<String> followingUsers = new ArrayList<>();

    //     try {
    //         connection = DatabaseConnector.connect();
    //         statement = connection.createStatement();
    //         String query = "SELECT followed_user FROM Follows WHERE follower_user = '" + username + "'";
    //         resultSet = statement.executeQuery(query);
    //         while (resultSet.next()) {
    //             followingUsers.add(resultSet.getString("followed_user"));
    //         }
    //     } finally {
    //         DatabaseConnector.close(connection, statement, resultSet);
    //     }

    //     return followingUsers;
    // }

    // public static int getFollowersCount(String username) throws Exception {
    //     Connection connection = null;
    //     Statement statement = null;
    //     ResultSet resultSet = null;
    //     int followersCount = 0;
    //     try {
    //         connection = DatabaseConnector.connect();
    //         statement = connection.createStatement();
    //         String query = "SELECT COUNT(*) AS count FROM Follows WHERE follower_user = '" + username + "'";
    //         resultSet = statement.executeQuery(query);
    //         if (resultSet.next()) {
    //             followersCount = resultSet.getInt("count");
    //         }
    //     } finally {
    //         DatabaseConnector.close(connection, statement, resultSet);
    //     }

    //     return followersCount;
    // }

    // public static int getPostCount(String username) throws Exception {
    //     Connection connection = null;
    //     Statement statement = null;
    //     ResultSet resultSet = null;
    //     int postCount = 0;
    //     try {
    //         connection = DatabaseConnector.connect();
    //         statement = connection.createStatement();
    //         String query = "SELECT COUNT(*) AS count FROM Posts WHERE username = '" + username + "'";
    //         resultSet = statement.executeQuery(query);
    //         if (resultSet.next()) {
    //             postCount = resultSet.getInt("count");
    //         }
    //     } finally {
    //         DatabaseConnector.close(connection, statement, resultSet);
    //     }

    //     return postCount;
    // }

    // public static void test(Statement s) throws SQLException {
    //     String query = "SELECT model " + "FROM products " + "WHERE maker = 'B'";
    //     ResultSet set = s.executeQuery(query);
    //     while (set.next()) {
    //         System.out.println(set.getNString(1));
    //     }
    // }

    // public static void main(String[] args) {
    //     System.out.println("I WORK");
    //     connect();
    // }
}