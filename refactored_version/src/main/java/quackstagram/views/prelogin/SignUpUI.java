package quackstagram.views.prelogin;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import quackstagram.controllers.prelogin.SignUpController;
import quackstagram.views.ColorID;

public class SignUpUI extends AbstractPreLogin {
    protected JTextField txtUsername;
    protected JTextField txtPassword;
    protected JTextField txtBio;
    protected JButton btnUploadPhoto;
    protected File selectedFile;
    protected SignUpController controller;

    public SignUpUI() {
        super("Sign Up");
        this.controller = new SignUpController(this);
    }

    @Override
    protected String getSecondButtonText() {
        return "Already have an account? Sign In";
    }

    @Override
    protected JPanel createMainContentPanel() {
        JPanel fieldsPanel = new JPanel();
        JPanel photoPanel = getDuckIcon();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        txtUsername = new JTextField("Username");
        txtPassword = new JTextField("Password");
        txtBio = new JTextField("Bio");
        txtBio.setForeground(getColor(ColorID.TEXT_SECONDARY));
        txtUsername.setForeground(getColor(ColorID.TEXT_SECONDARY));
        txtPassword.setForeground(getColor(ColorID.TEXT_SECONDARY));

        txtBio.setBackground(getColor(ColorID.ENTER_COMPONENT));
        txtUsername.setBackground(getColor(ColorID.ENTER_COMPONENT));
        txtPassword.setBackground(getColor(ColorID.ENTER_COMPONENT));

        // Remove the borders of the text fields
        txtBio.setBorder(null);
        txtUsername.setBorder(null);
        txtPassword.setBorder(null);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtBio);
        btnUploadPhoto = new JButton("Upload Photo");
        btnUploadPhoto.setForeground(getColor(ColorID.TEXT_PRIMARY));
        btnUploadPhoto.setBackground(getColor(ColorID.ENTER_COMPONENT));

        btnUploadPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleProfilePictureUpload();
            }
        });

        JButton twoFAButton = new JButton("Add 2FA");
        twoFAButton.setForeground(getColor(ColorID.TEXT_PRIMARY));
        twoFAButton.setBackground(getColor(ColorID.ENTER_COMPONENT));
        twoFAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.view.dispose();
                controller.view = new SignUpUIDecorator(controller.view);
                controller.showSignUp();
            }
        });

        JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        photoUploadPanel.add(btnUploadPhoto);
        photoUploadPanel.add(twoFAButton);
        photoUploadPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));
        
        fieldsPanel.add(photoUploadPanel);
        fieldsPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

        return fieldsPanel;
    }

    @Override
    protected void onPrimaryButtonClick(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String bio = txtBio.getText();

        if (this.selectedFile == null) {
            handleProfilePictureUpload();
        }

        if (this.selectedFile == null) {
            // User still hasnt selected the file, abort
            JOptionPane.showMessageDialog(this, "No file was selected. Please choose a file.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        controller.signUp(username, password, bio, selectedFile);
    }

    @Override
    protected void onSecondaryButtonCLick(ActionEvent event) {
        controller.showSignIn();
    }

    // Method to handle profile picture upload
    public void handleProfilePictureUpload() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        }
    }
}
