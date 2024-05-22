package quackstagram.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import quackstagram.models.Picture;

public class PictureRepository extends BaseRepository{

    public Picture getPictureById(String pictureId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            String query = "SELECT * FROM Posts WHERE post_id = ?";
            resultSet = executeQuery(connection, statement, query, pictureId);

            if (resultSet.next()) {
                return toPicture(resultSet);
            } else {
                throw new Exception("No such picture " + pictureId + " exists");
            }
        } finally {
            closeResources(connection, statement, resultSet);
        }
    }

    public ArrayList<Picture> getUserPictures(String username) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ArrayList<Picture> userPictures = new ArrayList<>();

        try {
            connection = getConnection();
            String query;
            if (username == null) {
                query = "SELECT * FROM Posts";
                statement = connection.prepareStatement(query);
            } else {
                query = "SELECT * FROM Posts WHERE username = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, username);
            }
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                userPictures.add(toPicture(resultSet));
            }
        } finally {
            closeResources(connection, statement, resultSet);
        }

        return userPictures;
    }

    public void savePicture(Picture picture) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = getConnection();
            String query = "INSERT INTO Posts (post_id, username, caption, day) VALUES (?, ?, ?, ?) " +
                           "ON DUPLICATE KEY UPDATE username = VALUES(username), caption = VALUES(caption), day = VALUES(day)";
            executeUpdate(connection, statement, query, picture.getPictureID(), picture.getOwner(), picture.getCaption(), picture.getDate());
        } finally {
            closeResources(connection, statement, null);
        }
    }

    private Picture toPicture(ResultSet resultSet) throws SQLException, Exception {
        String pictureId = resultSet.getString("post_id");
        String owner = resultSet.getString("username");
        String caption = resultSet.getString("caption");
        String date = resultSet.getTimestamp("day").toLocalDateTime().format(Picture.formatter);
    
        int likesCount = getLikesCount(pictureId);
    
        return new Picture(pictureId, owner, caption, date, likesCount);
    }    

    private int getLikesCount(String pictureId) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            String query = "SELECT COUNT(*) AS count FROM Likes WHERE post_id = ?";
            resultSet = executeQuery(connection, statement, query, pictureId);

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
