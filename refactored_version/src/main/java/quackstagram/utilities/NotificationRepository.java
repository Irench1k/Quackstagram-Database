package quackstagram.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import quackstagram.models.Notification;

public class NotificationRepository extends BaseRepository {

    public ArrayList<Notification> getNotifications(String username) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<Notification> userNotifications = new ArrayList<>();

        try {
            connection = getConnection();
            String query = "SELECT n.notification_id, n.post_id, n.sender_id, n.timestamp " +
                           "FROM Notifications n " +
                           "JOIN Posts p ON n.post_id = p.post_id " +
                           "WHERE p.username = ?";
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
            String query = "INSERT INTO Notifications (username, likedBy, pictureId, date) VALUES (?, ?, ?, ?)";
            executeUpdate(connection, statement, query, notification.getUsername(), notification.getLikedBy(),
                    notification.getPictureId(), notification.getDate());
        } finally {
            closeResources(connection, statement, null);
        }
    }

    private Notification toNotification(ResultSet resultSet, String username) throws SQLException {
        String likedBy = resultSet.getString("sender_id");
        String pictureId = resultSet.getString("post_id");
        String date = resultSet.getTimestamp("timestamp").toLocalDateTime().format(Notification.formatter);

        return new Notification(username, likedBy, pictureId, date);
    }

}
