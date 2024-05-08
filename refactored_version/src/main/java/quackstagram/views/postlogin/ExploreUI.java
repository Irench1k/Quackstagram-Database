package quackstagram.views.postlogin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import quackstagram.utilities.FileHandler;
import quackstagram.controllers.postlogin.ExploreController;
import quackstagram.models.Picture;
import quackstagram.models.User;
import quackstagram.views.ColorID;
import quackstagram.views.postlogin.commands.OpenExploreUICommand;

public class ExploreUI extends AbstractPostLogin {
    private final int WIDTH = AbstractPostLogin.WIDTH;
    static final int IMAGE_SIZE = 100;
    private ExploreController controller;

    /**
     * Represents the Explore user interface.
     */
    public ExploreUI(User currentUser) {
        super("Explore", currentUser);
        this.controller = new ExploreController(this, currentUser);
    }

    @Override
    protected JComponent createMainContentPanel() {
        // Create the main content panel with search and image grid
        // Search bar at the top
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(" Search Users");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height));

        // Image Grid
        JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2));
        // Load images into the panel
        loadImages(imageGridPanel);

        JScrollPane scrollPane = new JScrollPane(imageGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Main content panel that holds both the search bar and the image grid
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(searchPanel);
        mainContentPanel.add(scrollPane); // This will stretch to take up remaining space
        mainContentPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));
        mainContentPanel.setBorder(null);

        return mainContentPanel;
    }

    public void loadImages(JPanel imageGridPanel) {
        // Load images from the uploaded folder
        for (Picture picture : FileHandler.getUserPictures(null)) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(picture.getPath()).getImage()
                    .getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));

            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayImage(picture); // Call method to display the clicked image
                }
            });
            imageGridPanel.add(imageLabel);
        }
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
        usernameLabel.setForeground(getColor(ColorID.TEXT_PRIMARY));
        usernameLabel.setBackground(getColor(ColorID.MAIN_BACKGROUND));
        String timeSincePosting = getCalculatedTime(picture);
        JLabel timeLabel = new JLabel(timeSincePosting);
        timeLabel.setForeground(getColor(ColorID.TEXT_PRIMARY));
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);

        topPanel.add(usernameLabel, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);
        topPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

        usernameLabel.addActionListener(e -> {
            controller.goToUserProfile(picture.getOwner());
        });

        return topPanel;
    }

    private JLabel getImageLabel(Picture picture) {
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        try {
            BufferedImage originalImage = ImageIO.read(new File(picture.getPath()));
            ImageIcon imageIcon = new ImageIcon(originalImage);
            imageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            imageLabel.setText("Image not found");
        }
        imageLabel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

        return imageLabel;
    }

    private JPanel getBottomPanel(Picture picture) {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextArea captionArea = new JTextArea(picture.getCaption());
        captionArea.setForeground(getColor(ColorID.TEXT_PRIMARY));
        captionArea.setBackground(getColor(ColorID.MAIN_BACKGROUND));
        captionArea.setEditable(false);
        JLabel likesLabel = new JLabel("Likes: " + picture.getLikesCount());
        likesLabel.setForeground(getColor(ColorID.TEXT_PRIMARY));
        bottomPanel.add(captionArea, BorderLayout.CENTER);
        bottomPanel.add(likesLabel, BorderLayout.SOUTH);
        bottomPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

        return bottomPanel;
    }

    private JPanel getBackButtonPanel() {
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backButtonPanel.setForeground(getColor(ColorID.TEXT_PRIMARY));

        JButton backButton = new JButton("Back");
        // Make the button take up the full width
        backButton.setPreferredSize(new Dimension(WIDTH - 20, backButton.getPreferredSize().height));
        backButton.addActionListener(e -> {
            OpenExploreUICommand command = new OpenExploreUICommand(this);
            command.exploreUI(currentUser);
            this.dispose();
        });
        backButtonPanel.add(backButton);
        backButtonPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

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
        containerPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

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
