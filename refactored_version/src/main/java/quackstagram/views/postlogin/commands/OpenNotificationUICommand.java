package quackstagram.views.postlogin.commands;

import javax.swing.JFrame;

import quackstagram.models.User;
import quackstagram.views.postlogin.AbstractPostLogin;
import quackstagram.views.postlogin.NavigationCommand;
import quackstagram.views.postlogin.NotificationsUI;

public class OpenNotificationUICommand extends JFrame implements NavigationCommand {
    private AbstractPostLogin ui;

    public OpenNotificationUICommand(AbstractPostLogin ui) {
        this.ui = ui;
    }

    @Override
    public void execute(User currentUser) {
        notificationsUI(currentUser);
    }

    public void notificationsUI(User currentUser) {
        // Open InstagramProfileUI frame
        this.dispose();
        NotificationsUI notificationsUI = new NotificationsUI(currentUser);
        notificationsUI.setVisible(true);
    }
}
