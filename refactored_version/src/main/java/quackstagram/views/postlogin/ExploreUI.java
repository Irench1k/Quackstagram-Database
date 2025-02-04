package quackstagram.views.postlogin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import quackstagram.controllers.postlogin.ExploreController;
import quackstagram.models.Picture;
import quackstagram.models.User;
import quackstagram.utilities.DatabaseHandler;

public class ExploreUI extends AbstractPostLogin {

    private final int WIDTH = AbstractPostLogin.WIDTH;
    static final int IMAGE_SIZE = 90;
    private ExploreController controller;
    private int offset = 0;
    private final int limit = 30;
    private boolean isLoading = false;

    /**
     * Represents the Explore user interface.
     */
    public ExploreUI(User currentUser) {
        super("Explore", currentUser);
        this.controller = new ExploreController(this, currentUser);
    }

    @Override
    protected JComponent createMainContentPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(" Search Users");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));

        JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2));
        JScrollPane scrollPane = new JScrollPane(imageGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            if (!isLoading && !e.getValueIsAdjusting() && e.getValue() == e.getAdjustable().getMaximum() - scrollPane.getVerticalScrollBar().getModel().getExtent()) {
                loadImages(imageGridPanel);
            }
        });

        loadImages(imageGridPanel); // Load the initial set of images

        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(searchPanel);
        mainContentPanel.add(scrollPane);

        return mainContentPanel;
    }

    public void loadImages(JPanel imageGridPanel) {
        if (isLoading) return;
        isLoading = true;

        ArrayList<Picture> pictures = DatabaseHandler.getUserPictures(limit, offset, null);
        for (Picture picture : pictures) {
            ImageIcon imageIcon = new ImageIcon(picture.getPath());
            if (imageIcon.getIconWidth() == -1) { // Image file not found
                continue; // Skip this picture if image file is not found
            } else {
                imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
            }

            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayImage(picture); // Call method to display the clicked image
                }
            });
            imageGridPanel.add(imageLabel);
        }
        offset += limit;
        isLoading = false;
        imageGridPanel.revalidate();
        imageGridPanel.repaint();
    }

    private String getCalculatedTime(Picture picture) {
        String timeSincePosting = "Unknown";
        if (!picture.getDate().isEmpty()) {
            LocalDateTime timestamp = LocalDateTime.parse(picture.getDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime now = LocalDateTime.now();
            long days = ChronoUnit.DAYS.between(timestamp, now);
            timeSincePosting = days + " day" + (days != 1 ? "s" : "") + " ago";
        }

        return timeSincePosting;
    }

    private JPanel getTopPanel(Picture picture) {
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton usernameLabel = new JButton(picture.getOwner());
        String timeSincePosting = getCalculatedTime(picture);
        JLabel timeLabel = new JLabel(timeSincePosting);
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);

        topPanel.add(usernameLabel, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);

        usernameLabel.addActionListener(e -> {
            controller.goToUserProfile(picture.getOwner());
        });

        return topPanel;
    }

    private JLabel getImageLabel(Picture picture) {
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        ImageIcon imageIcon = new ImageIcon(picture.getPath());
        if (imageIcon.getIconWidth() == -1) { // Image file not found
            return null; // Return null if image file is not found
        } else {
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
        }
        imageLabel.setIcon(imageIcon);

        return imageLabel;
    }

    private JPanel getBottomPanel(Picture picture) {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextArea captionArea = new JTextArea(picture.getCaption());
        captionArea.setEditable(false);
        JLabel likesLabel = new JLabel("Likes: " + picture.getLikesCount());
        bottomPanel.add(captionArea, BorderLayout.CENTER);
        bottomPanel.add(likesLabel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    private JPanel getBackButtonPanel() {
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton backButton = new JButton("Back");
        // Make the button take up the full width
        backButton.setPreferredSize(new Dimension(WIDTH - 20, backButton.getPreferredSize().height));
        backButton.addActionListener(e -> {
            exploreUI();
        });
        backButtonPanel.add(backButton);

        return backButtonPanel;
    }

    private JPanel getContainerPanel(Picture picture) {
        JPanel containerPanel = new JPanel(new BorderLayout());

        JPanel topPanel = getTopPanel(picture);
        JLabel imageLabel = getImageLabel(picture);
        JPanel bottomPanel = getBottomPanel(picture);

        containerPanel.add(topPanel, BorderLayout.NORTH);
        containerPanel.add(imageLabel, BorderLayout.CENTER);
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);

        return containerPanel;
    }

    public void displayImage(Picture picture) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Add the container panel and back button panel to the frame
        add(getBackButtonPanel(), BorderLayout.NORTH);
        add(getContainerPanel(picture), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
}
