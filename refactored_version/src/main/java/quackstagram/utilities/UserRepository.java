package quackstagram.utilities;

import quackstagram.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends BaseRepository {

    public User getUser(String username) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            String query = "SELECT * FROM Users WHERE username = ?";
            resultSet = executeQuery(connection, statement, query, username);

            if (resultSet.next()) {
                return toUser(resultSet);
            } else {
                throw new Exception("No such user " + username + " exists");
            }
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    public void saveUser(User user) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            String query = "INSERT INTO Users (username, password, bio) VALUES (?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE password = VALUES(password), bio = VALUES(bio)";
            executeUpdate(connection, statement, query, user.getUsername(), user.getPassword(), user.getBio());
        } finally {
            closeResources(connection, statement, null);
        }
    }

    public void deleteUser(String username) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            String query = "DELETE FROM Users WHERE username = ?";
            executeUpdate(connection, statement, query, username);
        } finally {
            closeResources(connection, statement, null);
        }
    }

    private User toUser(ResultSet resultSet) throws Exception {
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String bio = resultSet.getString("bio");

        List<String> followingUsers = getFollowingUser(username);
        int followersCount = getFollowersCount(username);
        int postsCount = getPostCount(username);

        return new User(username, password, bio, followingUsers, followersCount, postsCount);
    }

    private List<String> getFollowingUser(String username) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<String> results = new ArrayList<>();

        try {
            connection = getConnection();
            String query = "SELECT followed_user FROM Follows WHERE follower_user = ?";
            resultSet = executeQuery(connection, statement, query, username);

            while (resultSet.next()) {
                results.add(resultSet.getString("followed_user"));
            }
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return results;
    }

    private int getFollowersCount(String username) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            String query = "SELECT COUNT(*) AS count FROM Follows WHERE followed_user = ?";
            resultSet = executeQuery(connection, statement, query, username);

            if (resultSet.next()) {
                return resultSet.getInt("count");
            } else {
                return 0;
            }
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    private int getPostCount(String username) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            String query = "SELECT COUNT(*) AS count FROM Posts WHERE username = ?";
            resultSet = executeQuery(connection, statement, query, username);

            if (resultSet.next()) {
                return resultSet.getInt("count");
            } else {
                return 0;
            }
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }
}
