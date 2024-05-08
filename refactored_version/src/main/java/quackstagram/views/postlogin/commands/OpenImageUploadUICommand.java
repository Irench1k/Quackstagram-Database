package quackstagram.views.postlogin.commands;

import javax.swing.JFrame;

import quackstagram.models.User;
import quackstagram.views.postlogin.AbstractPostLogin;
import quackstagram.views.postlogin.ImageUploadUI;
import quackstagram.views.postlogin.NavigationCommand;
import quackstagram.views.postlogin.QuakstagramHomeUI;

public class OpenImageUploadUICommand extends JFrame implements NavigationCommand {
    private AbstractPostLogin ui;

    public OpenImageUploadUICommand(AbstractPostLogin ui) {
        this.ui = ui;
    }

    @Override
    public void execute(User currentUser) {
        imageUploadUI(currentUser);
    }

    public void imageUploadUI(User currentUser) {
        // Open InstagramProfileUI frame
        this.dispose();
        ImageUploadUI upload = new ImageUploadUI(currentUser);
        upload.setVisible(true);
    }

}