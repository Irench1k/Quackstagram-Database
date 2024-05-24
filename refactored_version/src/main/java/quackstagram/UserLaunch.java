package quackstagram;

import javax.swing.SwingUtilities;

import quackstagram.views.prelogin.SignInUI;    

public class UserLaunch {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // String workingDirectory = System.getProperty("user.dir");
            // System.out.println("Working directory: " + workingDirectory);
            SignInUI frame = new SignInUI();
            frame.setVisible(true);
        });
    }
}
