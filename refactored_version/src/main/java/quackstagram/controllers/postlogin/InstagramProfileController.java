package quackstagram.controllers.postlogin;

import quackstagram.utilities.FileHandler;
import quackstagram.models.User;
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
        FileHandler.saveUser(this.currentUser);
    }
}
