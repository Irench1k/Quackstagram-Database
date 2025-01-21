package quackstagram.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import quackstagram.models.Notification;
import quackstagram.models.Picture;
import quackstagram.models.User;

public class DatabaseHandler {

    private static final Path PROFILE_PICTURE_DIR = Paths.get("img", "profile");
    private static final Path UPLOADS_PICTURE_DIR = Paths.get("img", "uploaded");
    private static UserRepository userRepository = new UserRepository();
    private static PictureRepository pictureRepository = new PictureRepository();
    private static NotificationRepository notificationRepository = new NotificationRepository();

    public static User getUser(String username) {
        User user = null;
        try {
            user = userRepository.getUser(username);
            System.out.println("User is " + user);
        } catch (Exception e) {
            System.err.println("Could not get user: " + e);
        }

        return user;
    }

    public static void saveUser(User user) {
        try {
            userRepository.saveUser(user);
        } catch (Exception e) {
            System.err.println("Could not save user: " + e);
        }
    }

    public static Picture getPictureById(String pictureId) throws Exception {
        Picture picture = null;
        try {
            picture = pictureRepository.getPictureById(pictureId);
        } catch (Exception e) {
            System.out.println("No such picture " + pictureId + " exist");
        }

        return picture;
    }

    public static ArrayList<Picture> getUserPictures(String username) {
        // Use a high limit for cases where username is not null
        int defaultLimit = (username == null) ? 50 : Integer.MAX_VALUE;
        int defaultOffset = 0;
        return getUserPictures(defaultLimit, defaultOffset, username);
    }

    public static ArrayList<Picture> getUserPictures(int limit, int offset, String username) {
        ArrayList<Picture> userPictures = new ArrayList<>();
        try {
            userPictures = pictureRepository.getUserPictures(username, limit, offset);
        } catch (Exception e) {
            System.err.println("Could not get user pictures: " + e);
        }
        return userPictures;
    }

    // Other methods...
    public static void savePicture(Picture picture) {
        try {
            pictureRepository.savePicture(picture);
        } catch (SQLException e) {
            // If triggers gets.. triggered?
            // That state is defined by the trigger, so we recognise it
            if (e.getSQLState().equals("45000")) {
                System.out.println("Could not save picture 1 : " + e.getMessage());
                System.out.println("The caption contains banned words."); // Maybe turn this into error message on
                                                                          // screen
            } else {
                System.out.println("Could not save picture 2 : " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Could not save picture 3 : " + e.getMessage());
        }
    }

    public static void uploadImage(File file, Picture picture) throws IOException {
        BufferedImage image = ImageIO.read(file);
        File outputFile = UPLOADS_PICTURE_DIR.resolve(picture.getPictureID() + ".png").toFile();
        ImageIO.write(image, "png", outputFile);
    }

    public static void uploadProfilePicture(File file, String username) throws IOException {
        BufferedImage image = ImageIO.read(file);
        File outputFile = PROFILE_PICTURE_DIR.resolve(username + ".png").toFile();
        ImageIO.write(image, "png", outputFile);
    }

    // All notifications *FOR SELECTED USER* (i.e. all notifications they should
    // see)
    public static ArrayList<Notification> getNotifications(String username) {
        ArrayList<Notification> userNotifications = new ArrayList<Notification>();
        try {
            userNotifications = notificationRepository.getNotifications(username);
        } catch (Exception e) {
            System.out.println("Could not parse notifications: " + e);
        }

        return userNotifications;
    }

    public static void saveNotification(Notification notification) {
        try {
            notificationRepository.saveNotification(notification);
        } catch (Exception e) {
            System.out.println("Could not save notification: " + e);
        }
    }
}
