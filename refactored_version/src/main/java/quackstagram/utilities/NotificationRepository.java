package quackstagram.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import quackstagram.models.Notification;

public class NotificationRepository extends BaseRepository {

    public ArrayList<Notification> getNotifications(String username) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<Notification> userNotifications = new ArrayList<>();

        try {
            connection = getConnection();
            String query = "SELECT l.notification_id, l.post_id, l.liker_user, l.timestamp "
                    + "FROM Likes l "
                    + "JOIN Posts p ON l.post_id = p.post_id "
                    + "WHERE p.username = ?";
            resultSet = executeQuery(connection, statement, query, username);

            while (resultSet.next()) {
                userNotifications.add(toNotification(resultSet, username));
            }
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return userNotifications;
    }

    public void saveNotification(Notification notification) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            String query = "INSERT INTO Likes (post_id, liker_user, timestamp) VALUES (?, ?, ?)";
            executeUpdate(connection, statement, query,
                    notification.getPictureId(), notification.getLikedBy(), notification.getDate());
        } finally {
            closeResources(connection, statement, null);
        }
    }

    private Notification toNotification(ResultSet resultSet, String username) throws SQLException {
        String likedBy = resultSet.getString("liker_user");
        String pictureId = resultSet.getString("post_id");
        String date = resultSet.getTimestamp("timestamp").toLocalDateTime().format(Notification.formatter);

        return new Notification(username, likedBy, pictureId, date);
    }

}
