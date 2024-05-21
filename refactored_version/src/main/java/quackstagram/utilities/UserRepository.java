package quackstagram.utilities;

import java.util.ArrayList;

import quackstagram.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserRepository extends BaseRepository {

    public User getUser(String username) throws Exception, SQLException {
        System.out.println("Calling get user");
        String query = "SELECT * FROM Users WHERE username = '" + username + "'";
        return toUser(executeQuery(query, username));
    }

    public void saveUser(User user) throws Exception {
        System.out.println("Calling save user");
        String query = "INSERT INTO Users (username, password, bio) VALUES (?, ?, ?) " +
                       "ON DUPLICATE KEY UPDATE password = VALUES(password), bio = VALUES(bio)";
        executeUpdate(query, user.getUsername(), user.getPassword(), user.getBio());
    }

    public void deleteUser(String username) throws Exception {
        System.out.println("Calling delete user");
        String query = "DELETE FROM Users WHERE username = ?";
        executeUpdate(query, username);
    }

    private User toUser(ResultSet resultSet) throws Exception {
        System.out.println("toUser got called here");
        String username = resultSet.getString("username");
        System.out.println("username: " + username);
        String password = resultSet.getString("password");
        System.out.println("password: " + password);
        String bio = resultSet.getString("bio");
        System.out.println("bio: " + bio);

        // Assuming that these methods are implemented and fetch necessary details from other tables
        List<String> followingUsers = getFollowingUser(username);
        System.out.println("Following user: " + followingUsers);
        int followersCount = getFollowersCount(username);
        System.out.println("Followers count: " + followersCount);
        int postsCount = getPostCount(username);
        System.out.println("Post count: " + postsCount);
        User user = new User(username, password, bio, followingUsers, followersCount, postsCount);
        System.out.println("User is " + user);

        return user;
    }

    private List<String> getFollowingUser(String username) throws Exception {
        String query = "SELECT followed_user FROM Follows WHERE follower_user = ?";
        List<String> results = new ArrayList<>();
        for (var user: executeQueryList(query, username)) {
            results.add(user.getString("followed_user"));
        }
        return results;
    }

    private int getFollowersCount(String username) throws Exception {
        String query = "SELECT COUNT(*) AS count FROM Follows WHERE followed_user = ?";
        ResultSet result = executeQuery(query, username);
        Integer count = result.getInt("count");
        
        return count != null ? count : 0;
    }

    private int getPostCount(String username) throws Exception {
        String query = "SELECT COUNT(*) AS count FROM Posts WHERE username = ?";
        ResultSet result = executeQuery(query, username);
        Integer count = result.getInt("count");

        return count != null ? count : 0;
    }
}

