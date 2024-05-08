package quackstagram.controllers.moderator;

import quackstagram.utilities.FileHandler;
import quackstagram.models.ModeratorModel;
import quackstagram.models.Picture;
import quackstagram.models.User;
import quackstagram.views.moderator.ModeratorView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code ModeratorController} class handles the interactions between the
 * {@link ModeratorModel} and {@link ModeratorView}. It processes user actions,
 * facilitates CRUD operations on user data, and updates the view accordingly.
 */
public class ModeratorController {
    private ModeratorModel model;
    private ModeratorView view;

    /**
     * Constructs a {@code ModeratorController} with the specified model and view.
     *
     * @param model the data model for moderator actions
     * @param view  the view that displays the moderator interface
     */
    public ModeratorController(ModeratorModel model, ModeratorView view) {
        this.model = model;
        this.view = view;
        view.setOnUserClickListener(this::onUserClicked);
        view.setOnAttributeClickListener(this::onAttributeClicked);
        updateView();
    }

    /**
     * Handles user selection in the view and updates the details panel
     * with user-specific information like bio and uploaded pictures.
     *
     * @param username the username of the selected user
     */
    private void onUserClicked(String username) {
        System.out.println("refreshing user details");  //TODO delete after debugging MM
        System.out.println("User = " + username);  //TODO delete after debugging MM
        User user = model.getUserByUsername(username);
        if (user != null) {
            List<String> userDetails = new ArrayList<>();
            userDetails.add("Username: " + user.getUsername());
            userDetails.add("Bio: " + user.getBio());

            List<Picture> pictures = model.getPicturesForUser(username);
            for (int i = 0; i < pictures.size(); i++) {
                Picture pic = pictures.get(i);
                String pictureText = "Picture " + (i + 1) + ": " + pic.getPictureID() + " - " + pic.getCaption();
                userDetails.add(pictureText);
            }

            view.setUserDetails(userDetails);
        }
    }

    /**
     * Handles attribute deletion requests such as bio, profile picture, or
     * uploaded pictures. Updates the view to reflect these changes.
     *
     * @param attribute the attribute to be processed for deletion
     */
    private void onAttributeClicked(String attribute) {
        String selectedUser = view.getSelectedUsername();
        if (selectedUser == null) return;

        try {
            if (attribute.startsWith("Bio: ")) {
                FileHandler.deleteUserBio(selectedUser);
                model.refreshUserList(); // Make sure the model has the latest data
                onUserClicked(selectedUser); // Now update the view
                System.out.println("Bio deleted for user: " + selectedUser);
            } else if (attribute.startsWith("Username: ")) {
                FileHandler.deleteUserProfilePicture(selectedUser);
                onUserClicked(selectedUser);
                System.out.println("Profile picture deleted for user: " + selectedUser);
            } else if (attribute.startsWith("Picture ")) {
                String[] parts = attribute.split(" - "); // Split on " - " which separates the ID from the caption
                if (parts.length > 1) {
                    String[] idParts = parts[0].split(": "); // Split the first part to get the ID
                    if (idParts.length > 1) {
                        String pictureID = idParts[1].trim();
                        FileHandler.deleteUserUploadedPicture(pictureID);
                        System.out.println("Uploaded picture deleted with ID: " + pictureID);
                    }
                }
            }
            // Refresh the user details
            onUserClicked(selectedUser);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error processing attribute click: " + e.getMessage());
        }
    }

    /**
     * Refreshes the list of users displayed in the moderator view.
     */
    private void updateView() {
        List<String> usernames = model.getUserList().stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
        view.displayUserList(usernames);
    }

    /**
     * Makes the moderator view visible to the user.
     */
    public void showView() {
        view.setVisible(true);
    }

}
