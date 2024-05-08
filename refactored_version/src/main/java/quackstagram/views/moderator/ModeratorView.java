package quackstagram.views.moderator;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Provides a visual interface for moderators to manage users' data in the Quackstagram application.
 * It allows moderators to browse through a list of users, view and delete user attributes such as bios,
 * profile pictures, and uploaded images. It also offers a preview feature for the uploaded images to
 * help moderators make informed decisions before deleting an item.
 */
public class ModeratorView extends JFrame {
    private JList<String> userListDisplay;
    private JList<String> userDetailsList;
    private DefaultListModel<String> userDetailsModel;
    private JButton deleteButton;
    private JLabel imagePreview;
    private Consumer<String> onUserClick;
    private Consumer<String> onAttributeClick;

    /**
     * Constructs a new {@code ModeratorView} setting up the main frame,
     * initializing components, and laying out the components in the panel.
     */
    public ModeratorView() {
        setTitle("Moderator Panel");
        setSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeComponents();
        layoutComponents();
    }

    /**
     * Sets up the components within the view, including lists to display user data and controls for interaction.
     * The image preview label is also initialized here but is not displayed until an image is selected.
     */
    private void initializeComponents() {
        userListDisplay = new JList<>();
        userListDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userListDisplay.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedUser = userListDisplay.getSelectedValue();
                if (selectedUser != null && onUserClick != null) {
                    onUserClick.accept(selectedUser);
                }
            }
        });

        userDetailsModel = new DefaultListModel<>();
        userDetailsList = new JList<>(userDetailsModel);
        userDetailsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userDetailsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateDeleteButtonVisibility();
                String selectedAttribute = userDetailsList.getSelectedValue();
                if (selectedAttribute != null && selectedAttribute.startsWith("Picture ")) {
                    loadImagePreview(selectedAttribute); // Load the image only when a picture is selected
                } else {
                    clearImagePreview(); // Clear the preview if the selected item is not a picture
                }
            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            String selectedAttribute = userDetailsList.getSelectedValue();
            if (selectedAttribute != null && onAttributeClick != null) {
                onAttributeClick.accept(selectedAttribute);
            }
        });
        deleteButton.setVisible(false); // The delete button is initially invisible

        imagePreview = new JLabel(); // Initialize the image preview label
        imagePreview.setPreferredSize(new Dimension(200, 200)); // Set a fixed size for the preview
    }

    /**
     * Clears the image preview display. This method is called when an image is deselected or a non-image item is selected.
     */
    private void clearImagePreview() {
        imagePreview.setIcon(null); // Remove the image from the label
    }

    /**
     * Arranges the components in the frame using BorderLayout. This method is responsible for positioning
     * the user list, user details list, image preview, and delete button within the main panel.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(new JScrollPane(userListDisplay), BorderLayout.WEST);
        add(new JScrollPane(userDetailsList), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(imagePreview, BorderLayout.NORTH);
        rightPanel.add(deleteButton, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);
    }

    /**
     * Populates the user list display with usernames. This method is usually called when the view needs to be refreshed,
     * such as after a user is added or removed.
     *
     * @param users List of usernames to be displayed in the user list.
     */
    public void displayUserList(List<String> users) {
        userListDisplay.setListData(users.toArray(new String[0]));
    }


    /**
     * Updates the details list for a selected user. It includes the user's bio, profile picture,
     * and uploaded pictures. If a picture is included in the details, it triggers the loading of the image preview.
     *
     * @param details List of user details including bio and pictures.
     */
    public void setUserDetails(List<String> details) {
        userDetailsModel.clear();
        for (String detail : details) {
            userDetailsModel.addElement(detail);
            if (detail.startsWith("Picture ")) {
                loadImagePreview(detail); // Attempt to load the image preview if this detail is a picture
            }
        }
        userDetailsList.revalidate();
        userDetailsList.repaint();
    }

    /**
     * Loads an image preview for the selected picture detail. This method handles the fetching and scaling
     * of the image to fit the preview area.
     *
     * @param pictureDetail Text detail of the selected picture used to locate the image file.
     */
    private void loadImagePreview(String pictureDetail) {
        // Extract the picture ID from the detail text and construct the file path
        String[] parts = pictureDetail.split(": ");
        if (parts.length > 1) {
            String pictureID = parts[1].split(" - ")[0];
            String filePath = "img/uploaded/" + pictureID + ".png";
            try {
                BufferedImage image = ImageIO.read(new File(filePath));
                ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(imagePreview.getWidth(), imagePreview.getHeight(), Image.SCALE_SMOOTH));
                imagePreview.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Registers a callback function to be invoked when a user is selected from the list.
     *
     * @param onUserClick Consumer functional interface that accepts a username upon selection.
     */
    public void setOnUserClickListener(Consumer<String> onUserClick) {
        this.onUserClick = onUserClick;
    }

    /**
     * Registers a callback function to be invoked when an attribute from the user details list is selected.
     *
     * @param onAttributeClick Consumer functional interface that accepts the detail string upon selection.
     */
    public void setOnAttributeClickListener(Consumer<String> onAttributeClick) {
        this.onAttributeClick = onAttributeClick;
    }

    /**
     * Updates the visibility of the delete button based on the selection in the user details list.
     */
    private void updateDeleteButtonVisibility() {
        boolean selectionExists = !userDetailsList.isSelectionEmpty();
        deleteButton.setVisible(selectionExists); // Show/hide the delete button based on selection
    }

    /**
     * Gets the username of the currently selected user in the user list.
     *
     * @return the username of the selected user, or null if no user is selected
     */
    public String getSelectedUsername() {
        return userListDisplay.getSelectedValue();
    }

}

