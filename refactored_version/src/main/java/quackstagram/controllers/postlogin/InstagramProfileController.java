package quackstagram.controllers.postlogin;

import quackstagram.utilities.*;
import quackstagram.models.User;
import quackstagram.utilities.DatabaseHandler;
import quackstagram.views.postlogin.InstagramProfileUI;

public class InstagramProfileController {
    private InstagramProfileUI view;
    private User currentUser;
    private User targetUser;

    public InstagramProfileController(InstagramProfileUI view, User currentUser, User targetUser) {
        this.view = view;
        this.currentUser = currentUser;
        this.targetUser = targetUser;
    }

    public void showProfileUI() {
        view.dispose();
        InstagramProfileUI profileUI = new InstagramProfileUI(currentUser, targetUser);
        profileUI.setVisible(true);
    }

    public void handleFollowAction() {
        this.currentUser.addUserToFollow(targetUser);
        DatabaseHandler.saveUser(this.currentUser);
    }
}