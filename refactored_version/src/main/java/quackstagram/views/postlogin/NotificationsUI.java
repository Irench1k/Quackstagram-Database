package quackstagram.views.postlogin;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import quackstagram.utilities.FileHandler;
import quackstagram.models.Notification;
import quackstagram.models.User;
import quackstagram.views.ColorID;

public class NotificationsUI extends AbstractPostLogin {
    private JComponent contentPanel;

    public NotificationsUI(User currentUser) {
        super("Notifications", currentUser);
        this.contentPanel = createMainContentPanel();
    }

    @Override
    protected JComponent createMainContentPanel() {
        // Content Panel for notifications
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setOpaque(false); // Make the viewport non-opaque
        scrollPane.getViewport().setBackground(getColor(ColorID.MAIN_BACKGROUND));

        updateNotifications();

        return scrollPane;
    }

    /**
     * Updates the notifications panel with the latest notifications for the current
     * user.
     * Removes all existing notifications from the panel and adds new notifications
     * retrieved from the file handler.
     * Each notification is displayed as a label within a panel.
     * The content panel is then revalidated and repainted to reflect the changes.
     * 
     * Gets updated by the Observer pattern when a new notification is added.
     */
    public void updateNotifications() {
        contentPanel.removeAll();

        for (Notification notification : FileHandler.getNotifications(getCurrentUser().getUsername())) {
            // Add the notification to the panel
            JPanel notificationPanel = new JPanel(new BorderLayout());
            notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            notificationPanel.setOpaque(false);  // Make the notification panels non-opaque
            notificationPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));

            JLabel notificationLabel = new JLabel(notification.getMessage());
            notificationLabel.setForeground(getColor(ColorID.TEXT_PRIMARY));
            notificationPanel.add(notificationLabel, BorderLayout.CENTER);

            contentPanel.add(notificationPanel);
        }

        contentPanel.setBackground(getColor(ColorID.MAIN_BACKGROUND));
        contentPanel.setBorder(null);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
