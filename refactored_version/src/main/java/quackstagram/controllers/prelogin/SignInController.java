package quackstagram.controllers.prelogin;

import quackstagram.utilities.*;
import quackstagram.models.User;
import quackstagram.utilities.DatabaseHandler;
import quackstagram.views.postlogin.InstagramProfileUI;
import quackstagram.views.prelogin.SignInUI;
import quackstagram.views.prelogin.SignUpUI;

public class SignInController {
    private SignInUI view;

    public SignInController(SignInUI view) {
        this.view = view;
    }

    public void logIn(String username, String password) {
        User user;
        try {
            user = DatabaseHandler.getUser(username);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (user.isPasswordEqual(password)) {
            showProfileUI(user);
        } else {
            System.out.println("Login Failed");
        }
    }

    protected void showProfileUI(User user) {
        view.dispose();
        InstagramProfileUI profileUI = new InstagramProfileUI(user);
        profileUI.setVisible(true);
    }

    public void showSignUp() {
        view.dispose();
        SignUpUI signUpFrame = new SignUpUI();
        signUpFrame.setVisible(true);
    }
}