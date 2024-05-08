package quackstagram.views.postlogin;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import quackstagram.utilities.FileHandler;
import quackstagram.controllers.postlogin.QuakstagramHomeController;
import quackstagram.models.Picture;
import quackstagram.models.User;
import quackstagram.views.ColorID;

/**
 * Represents the home screen of the Quakstagram application,
 * displaying a feed of image posts that users can interact with by liking
 * or viewing in more detail. The UI supports navigation to other parts
 * of the application like search, profile, and notifications.
 */
public class QuakstagramHomeUI extends AbstractPostLogin {

    private AbstractPostLogin ui;
    private NotificationsUI notificationsUI; // Add this line to declare the NotificationsUI instance
    private static final int IMAGE_WIDTH = WIDTH - 100; // Width for the image posts
    private static final int IMAGE_HEIGHT = 150; // Height for the image posts
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel homePanel;
    private JPanel imageViewPanel;
    private QuakstagramHomeController controller;

    /**
     * Initializes and sets up the Quakstagram home UI including layout, panels,
     * and navigation.
     */
    public QuakstagramHomeUI(User currentUser, NotificationsUI notificationsUI) {
        super("Quakstagram Home", currentUser);
        this.notificationsUI = notificationsUI; // Store the NotificationsUI instance in a field
        this.controller = new QuakstagramHomeController(this, currentUser);
    }

    @Override
    protected JComponent createMainContentPanel() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePanel = new JPanel(new BorderLayout());
        imageViewPanel = new JPanel(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ArrayList<Picture> picturesToShow = getPicturesToShow();
        populateContentPanel(contentPanel, picturesToShow);
        add(scrollPane, BorderLayout.CENTER);

        // Set up the home panel
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        homePanel.add(scrollPane, BorderLayout.CENTER);

        cardPanel.add(homePanel, "Home");
        cardPanel.add(imageViewPanel, "ImageView");
        cardLayout.show(cardPanel, "Home");

        contentPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));
        contentPanel.setBorder(null);

        return cardPanel;
    }

    @Override
    protected String getHeaderText() {
        return "🐥 Quakstagram 🐥";
    }

    private ArrayList<Picture> getPicturesToShow() {
        ArrayList<Picture> picturesToShow = new ArrayList<Picture>();
        ArrayList<String> users = currentUser.getFollowingUsers();
        for (String user : users) {
            picturesToShow.addAll(FileHandler.getUserPictures(user));
        }

        return picturesToShow;
    }

    private void populateContentPanel(JPanel panel, ArrayList<Picture> pictures) {

        for (Picture picture : pictures) {
            JPanel itemPanel = getItemPanel();

            JLabel nameLabel = getNameLabel(picture.getOwner());
            JLabel imageLabel = getImageLabel(picture.getPath());
            JLabel captionLabel = getCaptionLabel(picture.getCaption());
            JLabel likesLabel = getLikesLabel(picture.getLikesCount());
            JButton likeButton = getLikeButton(picture, likesLabel);
            JPanel spacingPanel = getSpacingPanel();

            nameLabel.setForeground(getColor(ColorID.TEXT_PRIMARY));
            imageLabel.setForeground(getColor(ColorID.TEXT_PRIMARY));
            captionLabel.setForeground(getColor(ColorID.TEXT_PRIMARY));
            likesLabel.setForeground(getColor(ColorID.TEXT_PRIMARY));

            itemPanel.add(nameLabel);
            itemPanel.add(imageLabel);
            itemPanel.add(captionLabel);
            itemPanel.add(likesLabel);
            itemPanel.add(likeButton);

            panel.add(itemPanel);
            panel.add(spacingPanel);

            // Make the image clickable
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayImage(picture); // Call a method to switch to the image view
                }
            });
        }
    }

    private JPanel getItemPanel() {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND)); // Set the background color for the item panel
        itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        itemPanel.setAlignmentX(CENTER_ALIGNMENT);
        return itemPanel;
    }

    private JLabel getNameLabel(String owner) {
        JLabel nameLabel = new JLabel(owner);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return nameLabel;
    }

    private JLabel getCaptionLabel(String caption) {
        JLabel captionLabel = new JLabel(caption);
        captionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return captionLabel;
    }

    private JLabel getLikesLabel(int count) {
        JLabel likesLabel = new JLabel("Likes: " + count);
        likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return likesLabel;
    }

    private JPanel getSpacingPanel() {
        // Grey spacing panel
        JPanel spacingPanel = new JPanel();
        spacingPanel.setPreferredSize(new Dimension(WIDTH - 10, 5)); // Set the height for spacing
        spacingPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND)); // Grey color for spacing
        return spacingPanel;
    }

    private JLabel getImageLabel(String picturePath) {
        // Crop the image to the fixed size
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to image label DO NOT CHANGE COLOR
        try {
            BufferedImage originalImage = ImageIO.read(new File(picturePath));
            BufferedImage croppedImage = originalImage.getSubimage(0, 0,
                    Math.min(originalImage.getWidth(), IMAGE_WIDTH),
                    Math.min(originalImage.getHeight(), IMAGE_HEIGHT));
            ImageIcon imageIcon = new ImageIcon(croppedImage);
            imageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            // Handle exception: Image file not found or reading error
            imageLabel.setText("Image not found");
        }

        return imageLabel;
    }

    private JPanel getUserPanel(Picture picture) {
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(picture.getOwner());
        userName.setForeground(getColor(ColorID.TEXT_PRIMARY));
        userName.setFont(new Font("Arial", Font.BOLD, 18));
        userPanel.add(userName);
        userPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

        return userPanel;
    }

    private JPanel getInfoPanel(Picture picture) {
        JLabel likesLabel = new JLabel(("Likes: " + picture.getLikesCount()));
        likesLabel.setForeground(getColor(ColorID.TEXT_PRIMARY));
        JButton likeButton = getLikeButton(picture, likesLabel);
        JLabel caption = new JLabel(picture.getCaption());
        caption.setForeground(getColor(ColorID.TEXT_PRIMARY));

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(caption);
        infoPanel.add(likesLabel);
        infoPanel.add(likeButton);
        infoPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

        return infoPanel;
    }

    private void displayImage(Picture picture) {
        imageViewPanel.removeAll(); // Clear previous content

        // Display the image
        JLabel fullSizeImageLabel = new JLabel();
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);

        try {
            BufferedImage originalImage = ImageIO.read(new File(picture.getPath()));
            BufferedImage croppedImage = originalImage.getSubimage(0, 0, Math.min(originalImage.getWidth(), WIDTH - 20),
                    Math.min(originalImage.getHeight(), HEIGHT - 40));
            ImageIcon imageIcon = new ImageIcon(croppedImage);
            fullSizeImageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            // Handle exception: Image file not found or reading error
            fullSizeImageLabel.setText("Image not found");
        }

        JPanel userPanel = getUserPanel(picture);
        JPanel infoPanel = getInfoPanel(picture);

        imageViewPanel.add(fullSizeImageLabel, BorderLayout.CENTER);
        imageViewPanel.add(infoPanel, BorderLayout.SOUTH);

        imageViewPanel.add(userPanel, BorderLayout.NORTH);

        imageViewPanel.revalidate();
        imageViewPanel.repaint();
        imageViewPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

        cardLayout.show(cardPanel, "ImageView"); // Switch to the image view
    }

    private JButton getLikeButton(Picture picture, JLabel likesLabel) {
        JButton likeButton = new JButton("❤");
        likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.setBackground(getColor(ColorID.LIKE_BUTTON)); // Set the background color for the like button
        //System.out.println("QUAKSTAGRAM Like button color: " + likeButtonColor2);
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false); // Remove border'
        
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newLikes = controller.addLike(picture, notificationsUI); // Pass the NotificationsUI instance
                likesLabel.setText("Likes: " + newLikes);
            }
        });
        
        return likeButton;
    }
}
