package quackstagram.controllers.postlogin;

import quackstagram.utilities.FileHandler;
import quackstagram.models.Notification;
import quackstagram.models.Picture;
import quackstagram.models.User;
import quackstagram.views.postlogin.NotificationsUI;
import quackstagram.views.postlogin.QuakstagramHomeUI;

public class QuakstagramHomeController {
    private QuakstagramHomeUI view;
    private User currentUser;

    public QuakstagramHomeController(QuakstagramHomeUI view, User currentUser) {
        this.view = view;
        this.currentUser = currentUser;
    }

    public int addLike(Picture picture, NotificationsUI notificationsUI) {
        // Add the like to the picture
        picture.addLike();
        FileHandler.savePicture(picture);
    
        // Check if the current user is the owner of the picture
        if (!currentUser.getUsername().equals(picture.getOwner())) {
            // Create a notification for the owner of the picture
            Notification notification = new Notification(picture.getOwner(), currentUser.getUsername(),
                    picture.getPictureID());
            notification.setNotificationsUI(notificationsUI);
            picture.addObserver(notification);
            FileHandler.saveNotification(notification);
        }
    
        return picture.getLikesCount();
    }
       
}
