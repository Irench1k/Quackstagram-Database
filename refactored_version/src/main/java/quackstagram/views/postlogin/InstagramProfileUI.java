package quackstagram.views.postlogin;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import quackstagram.utilities.FileHandler;
import quackstagram.controllers.postlogin.InstagramProfileController;
import quackstagram.models.Picture;
import quackstagram.models.User;
import quackstagram.views.ColorID;
import quackstagram.views.postlogin.components.InstagramUIComponents;

public class InstagramProfileUI extends AbstractPostLogin {
    private static final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images
    private InstagramUIComponents uiComponents;
    private JPanel contentPanel; // Panel to display the image grid or the clicked image
    private User targetUser;
    private InstagramProfileController controller;

    public InstagramProfileUI(User currentUser, User targetUser) {
        super("DACS Profile", currentUser);
        this.targetUser = targetUser;
        this.controller = new InstagramProfileController(this, currentUser, targetUser);
        this.uiComponents = new InstagramUIComponents(currentUser, targetUser, controller);
        overwriteMainContentPanel();
    }

    public InstagramProfileUI(User user) {
        this(user, user);
    }

    @Override
    protected JComponent createMainContentPanel() {
        this.contentPanel = new JPanel();
        return contentPanel;
    }

    private void overwriteMainContentPanel() {
        contentPanel.removeAll(); // Clear existing content
        contentPanel.setLayout(new GridLayout(0, 3, 5, 5)); // Grid layout for image grid

        for (Picture picture : FileHandler.getUserPictures(targetUser.getUsername())) {
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(picture.getPath()).getImage()
                    .getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
            contentPanel.add(createImageLabel(imageIcon));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        getContentPane().removeAll();
        setLayout(new BorderLayout());

        add(uiComponents.createHeaderPanel(), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        contentPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));
        contentPanel.setBorder(null);

        revalidate();
        repaint();
    }

    private JLabel createImageLabel(ImageIcon imageIcon) {
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayImage(imageIcon); // Call method to display the clicked image
            }
        });
        return imageLabel;
    }

    private void displayImage(ImageIcon imageIcon) {
        contentPanel.removeAll(); // Remove existing content
        contentPanel.setLayout(new BorderLayout()); // Change layout for image display

        JLabel fullSizeImageLabel = new JLabel(imageIcon);
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            controller.showProfileUI();
        });
        contentPanel.add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    @Override
    protected List<String> disabledIcons() {
        return List.of("profile");
    }
}