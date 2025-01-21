package quackstagram.controllers.postlogin;

import quackstagram.utilities.*;
import quackstagram.models.User;
import quackstagram.utilities.DatabaseHandler;
import quackstagram.views.postlogin.ExploreUI;
import quackstagram.views.postlogin.InstagramProfileUI;

public class ExploreController {
    private ExploreUI view;
    private User currentUser;

    public ExploreController(ExploreUI view, User currentUser) {
        this.view = view;
        this.currentUser = currentUser;
    }

    public void goToUserProfile(String username) {
        User pictureOwner;
        try {
            pictureOwner = DatabaseHandler.getUser(username);
        } catch (Exception error) {
            error.printStackTrace();
            return;
        }
        view.dispose();

        InstagramProfileUI profileUI = new InstagramProfileUI(currentUser, pictureOwner);
        profileUI.setVisible(true);
    }
}