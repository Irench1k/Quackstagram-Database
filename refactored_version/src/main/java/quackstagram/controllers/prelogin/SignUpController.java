package quackstagram.controllers.prelogin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import quackstagram.utilities.*;
import quackstagram.models.User;
import quackstagram.utilities.DatabaseHandler;
import quackstagram.views.prelogin.SignInUI;
import quackstagram.views.prelogin.SignUpUI;

public class SignUpController {
    private SignUpUI view;

    public SignUpController(SignUpUI view) {
        this.view = view;
    }

    public void signUp(String username, String password, String bio, File selectedFile) {
        try {
            DatabaseHandler.getUser(username);
            System.out.println("User: " + DatabaseHandler.getUser(username).getUsername());
            JOptionPane.showMessageDialog(view,
                    "Username already exists. Please choose a different username.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            // User does not exist - expected
        }

        User newUser = new User(username, password, bio, new ArrayList<String>(), 0, 0);

        // All checks done, save stuff
        // TODO: if one of the saving fails, revert has to be done
        DatabaseHandler.saveUser(newUser);
        saveProfilePicture(selectedFile, username);

        showSignIn();
    }

    private void saveProfilePicture(File file, String username) {
        try {
            DatabaseHandler.uploadProfilePicture(file, username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSignIn() {
        view.dispose();
        SignInUI signInFrame = new SignInUI();
        signInFrame.setVisible(true);
    }
}