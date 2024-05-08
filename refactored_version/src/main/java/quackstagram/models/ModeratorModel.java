package quackstagram.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.nio.file.Files;

/**
 * The {@code ModeratorModel} class represents the data model for the moderator panel.
 * It provides access to user and picture data, loading from and saving to persistent storage.
 */
public class ModeratorModel {
    private static final Path USERS_FILE = Paths.get("data", "users.txt");
    private static final Path PICTURES_FILE = Paths.get("data", "pictures.txt");
    private List<User> userList;

    /**
     * Constructs a new {@code ModeratorModel}, initializing the user list and loading users from the file.
     */
    public ModeratorModel() {
        userList = new ArrayList<>();
        loadUsers();
    }

    /**
     * Loads users from the {@code USERS_FILE} into the user list.
     */
    private void loadUsers() {
        try (BufferedReader reader = Files.newBufferedReader(USERS_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userDetails = line.split("; ");
                User user = User.createInstance(userDetails);
                userList.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the list of all users.
     *
     * @return a list of {@code User} objects
     */
    public List<User> getUserList() {
        return userList;
    }

    /**
     * Retrieves a {@code User} object by the username.
     *
     * @param username the username of the user to retrieve
     * @return the {@code User} object, or null if not found
     */
    public User getUserByUsername(String username) {
        return userList.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves a list of {@code Picture} objects associated with a username.
     *
     * @param username the username whose pictures are to be retrieved
     * @return a list of {@code Picture} objects
     */
    public List<Picture> getPicturesForUser(String username) {
        List<Picture> pictures = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(PICTURES_FILE)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] pictureDetails = line.split("; ");
                if (pictureDetails[1].equals(username)) {
                    pictures.add(Picture.createInstance(pictureDetails));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sort pictures by timestamp
        pictures.sort(Comparator.comparing(Picture::getDate).reversed());
        return pictures;
    }

    /**
     * Refreshes the list of users by re-loading them from the file.
     */
    public void refreshUserList() {
        userList.clear();
        loadUsers();
    }
}
