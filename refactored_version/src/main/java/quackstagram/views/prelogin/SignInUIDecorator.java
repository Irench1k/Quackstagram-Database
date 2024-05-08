package quackstagram.views.prelogin;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import quackstagram.views.ColorID;

import java.awt.event.ActionEvent;

public class SignInUIDecorator extends SignInUI {
    @SuppressWarnings("unused")
    private SignInUI signInUI;
    JTextField txtPassCode;

    public SignInUIDecorator(SignInUI signInUI) {
        super();
        this.signInUI = signInUI;
    }

    protected JPanel createMainContentPanel() {
        // Text fields panel
        JPanel fieldsPanel = new JPanel();
        JPanel photoPanel = getDuckIcon();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        txtUsername = new JTextField("Username");
        txtPassword = new JTextField("Password");
        txtPassCode = new JTextField("Passcode");
        txtPassCode.setForeground(getColor(ColorID.TEXT_SECONDARY));
        txtPassCode.setBackground(getColor(ColorID.ENTER_COMPONENT));
        txtUsername.setForeground(getColor(ColorID.TEXT_SECONDARY));
        txtUsername.setBackground(getColor(ColorID.ENTER_COMPONENT));
        txtPassword.setForeground(getColor(ColorID.TEXT_SECONDARY));
        txtPassword.setBackground(getColor(ColorID.ENTER_COMPONENT));

        txtPassCode.setBorder(BorderFactory.createEmptyBorder());
        txtPassword.setBorder(BorderFactory.createEmptyBorder());
        txtUsername.setBorder(BorderFactory.createEmptyBorder());
        
        
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(photoPanel);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassCode);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

        return fieldsPanel;
    }

    @Override
    protected void onPrimaryButtonClick(ActionEvent event) {
        controller.logIn(txtUsername.getText(), txtPassword.getText(), txtPassCode.getText());
    }
}
