package quackstagram.views.postlogin;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import quackstagram.controllers.postlogin.ImageUploadController;
import quackstagram.models.User;
import quackstagram.views.ColorID;

/**
 * The ImageUploadUI class represents a graphical user interface for uploading
 * images and entering captions.
 * It extends the JFrame class and provides methods for initializing the UI
 * components and handling user actions.
 * The UI consists of an image preview, a text area for entering captions, and
 * buttons for uploading images and saving captions.
 * The class also provides methods for reading the username, generating unique
 * image IDs, saving image information, and getting file extensions.
 */
public class ImageUploadUI extends AbstractPostLogin {
    private JLabel imagePreviewLabel;
    private JTextArea captionArea;
    private JButton uploadButton;
    private ImageUploadController controller;

    public ImageUploadUI(User currentUser) {
        super("Upload Image", currentUser);
        controller = new ImageUploadController(this, currentUser);
    }

    /**
     * Initializes the user interface for the image upload functionality.
     * This method creates and configures the necessary UI components, such as
     * panels, labels, text areas, and buttons.
     * It sets up the layout and adds the components to the frame.
     */
    protected JComponent createMainContentPanel() {
        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        createImageIcon(contentPanel);
        createCaptionTextAndPane(contentPanel);
        createUploadButton(contentPanel);
        contentPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

        return contentPanel;
    }

    private void createUploadButton(JPanel contentPanel) {
        String upload = "Upload Image";
        uploadButton = new JButton(upload);
        uploadButton.setForeground(getColor(ColorID.TEXT_PRIMARY));
        uploadButton.setBackground(getColor(ColorID.LIKE_BUTTON));
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener((e) -> controller.uploadAction(captionArea.getText()));
        contentPanel.add(uploadButton);
    }

    private void createCaptionTextAndPane(JPanel contentPanel) {
        captionArea = new JTextArea("Enter a caption");
        captionArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        captionArea.setLineWrap(true);
        captionArea.setWrapStyleWord(true);
        captionArea.setForeground(getColor(ColorID.TEXT_PRIMARY));
        captionArea.setBackground(getColor(ColorID.ENTER_COMPONENT));


        JScrollPane bioScrollPane = new JScrollPane(captionArea);
        bioScrollPane.setBackground(getColor(ColorID.ENTER_COMPONENT));
        bioScrollPane.setBorder(null);
        bioScrollPane.setPreferredSize(new Dimension(WIDTH - 50, HEIGHT / 6));
        contentPanel.add(bioScrollPane);
    }

    private void createImageIcon(JPanel contentPanel) {
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePreviewLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));

        // Set an initial empty icon to the imagePreviewLabel
        ImageIcon emptyImageIcon = new ImageIcon();
        imagePreviewLabel.setIcon(emptyImageIcon);

        contentPanel.add(imagePreviewLabel);
    }

    @Override
    protected List<String> disabledIcons() {
        return List.of("add");
    }
}
