package quackstagram;

import quackstagram.controllers.moderator.ModeratorController;
import quackstagram.models.ModeratorModel;
import quackstagram.views.moderator.ModeratorView;

import javax.swing.*;


/**
 * The entry point for the Moderator Panel application in the Quackstagram project.
 * Initializes the model, view, and controller for the moderator interface and
 * makes the view visible to the user.
 * <p>
 * This class contains the main method which sets up the application. The main
 * method is responsible for creating instances of the ModeratorModel,
 * ModeratorView, and ModeratorController. It then triggers the display of the
 * ModeratorView, allowing the moderator to interact with the application.
 * </p>
 *
 * TODO: add delete full user (if we have time)
 */
public class ModeratorLaunch {

    /**
     * The main method that initializes and runs the moderator panel.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ModeratorModel model = new ModeratorModel();
            ModeratorView view = new ModeratorView();
            ModeratorController controller = new ModeratorController(model, view);
            controller.showView();
        });
    }
}