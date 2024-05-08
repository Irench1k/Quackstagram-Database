package quackstagram.views.postlogin.commands;

import javax.swing.JFrame;

import quackstagram.models.User;
import quackstagram.views.postlogin.AbstractPostLogin;
import quackstagram.views.postlogin.ExploreUI;
import quackstagram.views.postlogin.NavigationCommand;
import quackstagram.views.postlogin.QuakstagramHomeUI;

public class OpenExploreUICommand extends JFrame implements NavigationCommand {
    private AbstractPostLogin ui;

    public OpenExploreUICommand(AbstractPostLogin ui) {
        this.ui = ui;
    }

    @Override
    public void execute(User currentUser) {
        exploreUI(currentUser);
    }

    public void exploreUI(User currentUser) {
        // Open InstagramProfileUI frame
        this.dispose();
        ExploreUI explore = new ExploreUI(currentUser);
        explore.setVisible(true);
    }
}