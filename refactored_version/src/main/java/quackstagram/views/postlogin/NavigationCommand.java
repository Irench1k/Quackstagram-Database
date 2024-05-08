package quackstagram.views.postlogin;

import quackstagram.models.User;

public interface NavigationCommand {
    void execute(User currentUser);
}
