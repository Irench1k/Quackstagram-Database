package quackstagram.views.postlogin.commands;

import javax.swing.JFrame;

import quackstagram.models.User;
import quackstagram.views.postlogin.AbstractPostLogin;
import quackstagram.views.postlogin.NavigationCommand;
import quackstagram.views.postlogin.NotificationsUI;
import quackstagram.views.postlogin.QuakstagramHomeUI;

public class OpenHomeUICommand extends JFrame implements NavigationCommand {
    private AbstractPostLogin ui;

    public OpenHomeUICommand(AbstractPostLogin ui) {
        this.ui = ui;
    }

    @Override
    public void execute(User currentUser) {
        openHomeUI(currentUser);
    }

    public void openHomeUI(User currentUser) {
        // Open InstagramProfileUI frame
        this.dispose();
        NotificationsUI notificationsUI = new NotificationsUI(currentUser);
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI(currentUser, notificationsUI);
        homeUI.setVisible(true);
    }
}