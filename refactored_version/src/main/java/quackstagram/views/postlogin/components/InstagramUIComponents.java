package quackstagram.views.postlogin.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import quackstagram.controllers.postlogin.InstagramProfileController;
import quackstagram.models.User;
import quackstagram.views.ColorID;
import quackstagram.views.Theme;
import quackstagram.views.postlogin.InstagramProfileUI;

public class InstagramUIComponents {
    private User currentUser; // logged in user
    private User targetUser; // user whos profile is displayed
    private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    private boolean isCurrentUser = false;
    private InstagramProfileController controller;
    private Theme theme = Theme.getInstance();
    private Color followButtonColor = theme.getColor(ColorID.FOLLOW_BUTTON);
    private Color textPrimaryColor = theme.getColor(ColorID.TEXT_PRIMARY);
    private Color backgroundHeaderSecondary = theme.getColor(ColorID.BACKGROUND_HEADER_SECONDARY);
    private Color minorBackgroundColor = theme.getColor(ColorID.MINOR_BACKGROUND);


    public InstagramUIComponents(User currentUser, User targetUser, InstagramProfileController controller) {
        this.currentUser = currentUser;
        this.targetUser = targetUser;
        this.controller = controller;
        this.isCurrentUser = currentUser.isIdEqualTo(targetUser);
    }

    public JPanel createHeaderPanel() {
        // Header Panel
        JPanel headerPanel = new JPanel();

        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(backgroundHeaderSecondary);

        // Top Part of the Header (Profile Image, Stats, Follow Button)
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(minorBackgroundColor);

        // Profile image
        ImageIcon profileIcon = new ImageIcon(
                new ImageIcon(targetUser.getProfileImagePath())
                        .getImage()
                        .getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);

        // Stats Panel
        JPanel statsPanel = createStatePanel();

        // Follow Button
        JButton followButton = createFollowButton();

        // Add Stats and Follow Button to a combined Panel
        JPanel statsFollowPanel = createStatsFollowPanel(statsPanel, followButton);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        headerPanel.add(topHeaderPanel);

        // Profile Name and Bio Panel
        JPanel profileNameAndBioPanel = createProfileNameAndBioPanel();

        // Profile Name Label
        JLabel profileNameLabel = createProfileNameLabel();

        // Profile Bio
        JTextArea profileBio = createProfileBio();

        profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
        profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

        headerPanel.add(profileNameAndBioPanel);

        return headerPanel;
    }

    private JLabel createProfileNameLabel() {
        JLabel profileNameLabel = new JLabel(targetUser.getUsername());
        profileNameLabel.setForeground(textPrimaryColor);
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides
        return profileNameLabel;
    }

    private JPanel createProfileNameAndBioPanel() {
        JPanel profileNameAndBioPanel = new JPanel();
        profileNameAndBioPanel.setLayout(new BorderLayout());
        profileNameAndBioPanel.setBackground(minorBackgroundColor);
        return profileNameAndBioPanel;
    }

    private JTextArea createProfileBio() {
        JTextArea profileBio = new JTextArea(targetUser.getBio());
        profileBio.setForeground(textPrimaryColor);
        System.out.println("This is the bio " + targetUser.getUsername());
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(minorBackgroundColor);
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides
        return profileBio;
    }

    private JPanel createStatsFollowPanel(JPanel statsPanel, JButton followButton) {
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(followButton);
        return statsFollowPanel;
    }

    private JButton createFollowButton() {
        JButton followButton;
        if (this.isCurrentUser) {
            followButton = new JButton("Edit Profile ⤵");
            followButton.addActionListener(e -> showThemeSelectionMenu(followButton));
        } else {
            // Check if the current user is already being followed by the logged-in user
            if (this.currentUser.followsUser(targetUser)) {
                followButton = new JButton("Following");
            } else {
                followButton = new JButton("Follow");
                followButton.addActionListener(e -> {
                    controller.handleFollowAction();
                    followButton.setText("Following");
                });
            }
        }
        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followButton.setFont(new Font("Arial", Font.BOLD, 12));
        followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height));
        followButton.setBackground(followButtonColor); // Gray button
        followButton.setForeground(textPrimaryColor);
        followButton.setOpaque(true);
        followButton.setBorderPainted(false);
        followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return followButton;
    }

    private void showThemeSelectionMenu(JButton source) {
        JPopupMenu themeMenu = new JPopupMenu();

            // Iterate over all available themes
        for (Theme.ThemeName themeName : Theme.ThemeName.values()) {
            JMenuItem themeItem = new JMenuItem(themeName.name());
            themeItem.addActionListener(e -> {
                Theme.getInstance().changeTheme(themeName);
                controller.showProfileUI(); // Refresh the profile UI to apply the new theme
            });
            themeMenu.add(themeItem);
        }

        themeMenu.show(source, 0, source.getHeight());
    }

    private JPanel createStatePanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(minorBackgroundColor);
        System.out.println("Number of posts for this user" + targetUser.getPostsCount());
        statsPanel.add(createStatLabel(Integer.toString(targetUser.getPostsCount()), "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(targetUser.getFollowersCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(targetUser.getFollowingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding
        return statsPanel;
    }

    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>",
                SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(textPrimaryColor);
        return label;
    }
}