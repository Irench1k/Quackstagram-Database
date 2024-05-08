package quackstagram.views.postlogin;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import quackstagram.models.User;
import quackstagram.views.BaseFrameManager;
import quackstagram.views.ColorID;
import quackstagram.views.IconID;
import quackstagram.views.postlogin.commands.*;

// Common ancestor of all post-auth Views
public abstract class AbstractPostLogin extends BaseFrameManager {
    protected static final int NAV_ICON_SIZE = 20;
    protected User currentUser;

    public AbstractPostLogin(String title, User currentUser) {
        super(title);
        this.currentUser = currentUser;
        initializeUI();
    }

    protected User getCurrentUser() {
        return this.currentUser;
    }

    @Override
    protected abstract JComponent createMainContentPanel();

    @Override
    protected JComponent createControlPanel() {
        // Create and return the navigation panel
        // Navigation Bar
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(getColor(ColorID.MINOR_BACKGROUND));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        navigationPanel.add(createIconButton(getIconPath(IconID.HOME), "home"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton(getIconPath(IconID.SEARCH), "explore"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton(getIconPath(IconID.ADD), "add"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton(getIconPath(IconID.HEART), "notification"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton(getIconPath(IconID.PROFILE), "profile"));

        return navigationPanel;
    }

    protected List<String> disabledIcons() {
        return new ArrayList<String>();
    }

    private JButton createIconButton(String iconPath, String buttonType) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);

        if (!disabledIcons().contains(buttonType)) {
            button.addActionListener(e -> performActionBasedOnButtonType(buttonType));
        }

        return button;
    }

    private void performActionBasedOnButtonType(String buttonType) {
        NavigationCommand command = null;

        switch (buttonType) {
            case "home":
                command = new OpenHomeUICommand(this);
                break;
            case "profile":
                command = new OpenInstagramProfileUICommand(this);
                break;
            case "notification":
                command = new OpenNotificationUICommand(this);
                break;
            case "explore":
                command = new OpenExploreUICommand(this);
                break;
            case "add":
                command = new OpenImageUploadUICommand(this);
                break;
            default:
                break;
        }
        if (command != null) {
            command.execute(currentUser);
            this.dispose();
        }
    }








}
