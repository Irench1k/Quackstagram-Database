package quackstagram.views.prelogin;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import quackstagram.views.ColorID;

public class SignUpUIDecorator extends SignUpUI {
    SignUpUI signUpUI;
    JTextField twoFAField;

    public SignUpUIDecorator(SignUpUI signUpUI) {
        super();
        this.signUpUI = signUpUI;
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
        txtBio.setBackground(getColor(ColorID.ENTER_COMPONENT));
        txtUsername.setForeground(getColor(ColorID.TEXT_SECONDARY));
        txtUsername.setBackground(getColor(ColorID.ENTER_COMPONENT));
        txtPassword.setForeground(getColor(ColorID.TEXT_SECONDARY));
        txtPassword.setBackground(getColor(ColorID.ENTER_COMPONENT));

        txtBio.setBorder(BorderFactory.createEmptyBorder());
        txtPassword.setBorder(BorderFactory.createEmptyBorder());
        txtUsername.setBorder(BorderFactory.createEmptyBorder());
        
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtBio);
        fieldsPanel.add(Box.createVerticalStrut(10));
        btnUploadPhoto = new JButton("Upload Photo");
        btnUploadPhoto.setForeground(getColor(ColorID.TEXT_PRIMARY));
        btnUploadPhoto.setBackground(getColor(ColorID.ENTER_COMPONENT));
        
        btnUploadPhoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleProfilePictureUpload();
            }
        });
        
        twoFAField = new JTextField("Passcode");
        twoFAField.setForeground(getColor(ColorID.TEXT_SECONDARY));
        twoFAField.setBackground(getColor(ColorID.ENTER_COMPONENT));
        twoFAField.setBorder(BorderFactory.createEmptyBorder());
        fieldsPanel.add(twoFAField);

        JPanel photoUploadPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        photoUploadPanel.setForeground(getColor(ColorID.TEXT_SECONDARY));
        photoUploadPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));
        photoUploadPanel.add(btnUploadPhoto);
        
        fieldsPanel.add(photoUploadPanel);
        fieldsPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));


        return fieldsPanel;
    }

    @Override
    protected void onPrimaryButtonClick(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String bio = txtBio.getText();
        String passCode = twoFAField.getText();

        if (this.selectedFile == null) {
            handleProfilePictureUpload();
        }

        if (this.selectedFile == null) {
            // User still hasnt selected the file, abort
            JOptionPane.showMessageDialog(this, "No file was selected. Please choose a file.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        controller.signUp(username, password, bio, passCode, selectedFile);
    }
}
