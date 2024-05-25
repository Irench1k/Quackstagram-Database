package quackstagram.views.postlogin;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import quackstagram.utilities.*;
import quackstagram.models.Notification;
import quackstagram.models.User;
import quackstagram.utilities.DatabaseHandler;

public class NotificationsUI extends AbstractPostLogin {

    public NotificationsUI(User currentUser) {
        super("Notifications", currentUser);
    }

    @Override
    protected JComponent createMainContentPanel() {
        // Content Panel for notifications
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        for (Notification notification : DatabaseHandler.getNotifications(getCurrentUser().getUsername())) {
            // Add the notification to the panel
            JPanel notificationPanel = new JPanel(new BorderLayout());
            notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            JLabel notificationLabel = new JLabel(notification.getMessage());
            notificationPanel.add(notificationLabel, BorderLayout.CENTER);

            contentPanel.add(notificationPanel);
        }

        return scrollPane;
    }
}