package quackstagram.views.postlogin.commands;

import javax.swing.JFrame;

import quackstagram.models.User;
import quackstagram.views.postlogin.AbstractPostLogin;
import quackstagram.views.postlogin.InstagramProfileUI;
import quackstagram.views.postlogin.NavigationCommand;

public class OpenInstagramProfileUICommand extends JFrame implements NavigationCommand {
    private AbstractPostLogin ui;

    public OpenInstagramProfileUICommand(AbstractPostLogin ui) {
        this.ui = ui;
    }

    @Override
    public void execute(User currentUser) {
        openProfileUI(currentUser);
    }

    public void openProfileUI(User currentUser) {
        // Open InstagramProfileUI frame
        this.dispose();
        InstagramProfileUI profileUI = new InstagramProfileUI(currentUser, currentUser);
        profileUI.setVisible(true);
    }
}
