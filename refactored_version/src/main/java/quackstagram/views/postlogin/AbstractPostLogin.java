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
        navigationPanel.setBackground(new Color(249, 249, 249));
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        navigationPanel.add(createIconButton("img/icons/home.png", "home"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/search.png", "explore"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/add.png", "add"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/heart.png", "notification"));
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(createIconButton("img/icons/profile.png", "profile"));

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
        switch (buttonType) {
            case "home":
                openHomeUI();
                break;
            case "profile":
                openProfileUI();
                break;
            case "notification":
                notificationsUI();
                break;
            case "explore":
                exploreUI();
                break;
            case "add":
                imageUploadUI();
                break;
            default:
                break;
        }
    }

    protected void imageUploadUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        ImageUploadUI upload = new ImageUploadUI(currentUser);
        upload.setVisible(true);
    }

    protected void openProfileUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        InstagramProfileUI profileUI = new InstagramProfileUI(currentUser, currentUser);
        profileUI.setVisible(true);
    }

    protected void notificationsUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        NotificationsUI notificationsUI = new NotificationsUI(currentUser);
        notificationsUI.setVisible(true);
    }

    protected void openHomeUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI(currentUser);
        homeUI.setVisible(true);
    }

    protected void exploreUI() {
        // Open InstagramProfileUI frame
        this.dispose();
        ExploreUI explore = new ExploreUI(currentUser);
        explore.setVisible(true);
    }
}

